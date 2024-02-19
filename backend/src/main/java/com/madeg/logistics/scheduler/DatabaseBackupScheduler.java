package com.madeg.logistics.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class DatabaseBackupScheduler {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseBackupScheduler.class);
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // @Scheduled(cron = "*/10 * * * * ?")
    @Scheduled(cron = "0 1 * * * 0")
    public void backupDatabase() {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String backupFileName = "db_backup_" + formattedDate + ".sql";
        String backupPath = "C:/Users/yeahee/Desktop/project/logistics/backend/src/main/resources/";
        String tmpBackupPath = "/tmp/" + backupFileName;

        String[] cmd = {
                "cmd.exe", "/c",
                "docker exec madeg_postgres pg_dump -U postgres -d postgres -f " + tmpBackupPath +
                        " && docker cp madeg_postgres:" + tmpBackupPath + " " + backupPath + backupFileName
        };

        ProcessBuilder pb = new ProcessBuilder(cmd);
        try {
            Process process = pb.start();
            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (finished && process.exitValue() == 0) {
                logger.info("Database backup created successfully at " + backupPath + backupFileName);
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
