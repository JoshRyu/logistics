package com.madeg.logistics.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.madeg.logistics.util.DatabaseUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    // @Scheduled(cron = "*/15 * * * * ?") //FOR TEST
    @Scheduled(cron = "0 1 * * * 0") // Every Sunday 1AM
    public void backupDatabase() {

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String backupFileName = "db_backup_" + formattedDate + ".sql";
        String tmpBackupPath = "/tmp/" + backupFileName;

        String basePath = databaseUtil.getBasePath();
        String backupPath = databaseUtil.getBackupPath(winBackupPath, linuxBackupPath);
        String fullPath = basePath + backupPath;
        String dbName = databaseUtil.getDbName(dbUrl);

        String[] winCmd = {
                "cmd.exe", "/c",
                "docker exec madeg_postgres pg_dump -U " + userName + " -d " + dbName + " -f " + tmpBackupPath +
                        " && docker cp madeg_postgres:" + tmpBackupPath + " " + fullPath + backupFileName
        };

        String[] linuxCmd = {
                "/bin/bash", "-c",
                "sudo -u postgres pg_dump -U " + userName + " -d " + dbName + " -f " + backupPath + backupFileName
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

}
