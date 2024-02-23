package com.madeg.logistics.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.madeg.logistics.util.DatabaseUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
public class DatabaseBackupScheduler {

    @Autowired
    private DatabaseUtil databaseUtil;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${backup.path.win}")
    private String winBackupPath;

    @Value("${backup.path.linux}")
    private String linuxBackupPath;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseBackupScheduler.class);
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // @Scheduled(cron = "*/15 * * * * ?") // FOR TEST
    @Scheduled(cron = "0 1 * * * 0") // Every Sunday at 1AM
    public void backupDatabase() {
        String backupFileName = "db_backup_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".sql";
        String dbName = databaseUtil.getDbName(dbUrl);
        String fullPath = databaseUtil.getFullBackupPath(winBackupPath, linuxBackupPath);

        String[] winCmd = {
                "cmd.exe", "/c",
                "docker exec madeg_postgres pg_dump -U " + userName + " -d " + dbName + " -f " + "/tmp/"
                        + backupFileName +
                        " && docker cp madeg_postgres:" + "/tmp/" + backupFileName + " " + fullPath + backupFileName
        };

        String[] linuxCmd = {
                "/bin/bash", "-c",
                "sudo -u postgres pg_dump -U " + userName + " -d " + dbName + " -f " + fullPath + backupFileName
        };

        String[] cmd = System.getProperty("os.name").toLowerCase().contains("win") ? winCmd : linuxCmd;
        ProcessBuilder pb = new ProcessBuilder(cmd);
        try {
            Process process = pb.start();
            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (finished && process.exitValue() == 0) {
                logger.info("Database backup created successfully at " + fullPath + backupFileName);
            } else {
                logger.error("Error occurred during database backup or timeout reached.");
            }
        } catch (InterruptedException | IOException e) {
            logger.error("Exception occurred during backup process", e);
        } finally {
            executor.shutdownNow();
        }
    }

    // @Scheduled(cron = "*/15 * * * * ?") // FOR TEST
    @Scheduled(cron = "0 2 * * * 0") // Every Sunday at 2AM
    public void deleteOldBackups() {
        String fullPath = databaseUtil.getFullBackupPath(winBackupPath, linuxBackupPath);
        LocalDate threeMonthAgo = LocalDate.now().minusMonths(3);

        try (Stream<Path> files = Files.walk(Paths.get(fullPath))) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".sql"))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        String datePart = fileName.replace("db_backup_", "").replace(".sql", "");
                        try {
                            LocalDate fileDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyyMMdd"));
                            if (fileDate.isBefore(threeMonthAgo)) {
                                File file = path.toFile();
                                if (file.delete()) {
                                    logger.info("Deleted old backup file: " + file.getPath());
                                } else {
                                    logger.error("Failed to delete file: " + file.getPath());
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error deleting file: " + path, e);
                        }
                    });
        } catch (IOException e) {
            logger.error("Error accessing backup directory: " + fullPath, e);
        }
    }

}
