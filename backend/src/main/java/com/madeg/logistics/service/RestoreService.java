package com.madeg.logistics.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RestoreService {

    private static final Logger logger = LoggerFactory.getLogger(RestoreService.class);

    public void restoreDatabase(String backupFileName) {
        String backupPath = "C:/Users/yeahee/Desktop/project/logistics/backend/src/main/resources/";
        String containerPath = "/tmp/" + backupFileName;
        String[] copyCmd = {
                "cmd.exe", "/c",
                "docker cp " + backupPath + backupFileName + " madeg_postgres:" + containerPath
        };
        String[] restoreCmd = {
                "cmd.exe", "/c",
                "docker exec -i madeg_postgres psql -U postgres -d postgres -f " + containerPath
        };

        ProcessBuilder copyPb = new ProcessBuilder(copyCmd);
        ProcessBuilder restorePb = new ProcessBuilder(restoreCmd);
        try {
            Process copyProcess = copyPb.start();
            copyProcess.waitFor(60, TimeUnit.SECONDS);

            Process restoreProcess = restorePb.start();
            boolean finishedRestore = restoreProcess.waitFor(60, TimeUnit.SECONDS);

            if (finishedRestore && restoreProcess.exitValue() == 0) {
                logger.info("Database restored successfully from " + backupPath);
            } else {
                logger.error("Error occurred during database restoration or timeout reached.");
            }
        } catch (InterruptedException | IOException e) {
            logger.error("Exception occurred during restore process", e);
        }
    }

}
