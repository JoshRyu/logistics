package com.madeg.logistics.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.madeg.logistics.util.DatabaseUtil;

@Service
public class RestoreService {

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

    private static final Logger logger = LoggerFactory.getLogger(RestoreService.class);

    public void restoreDatabase(String backupFileName) {

        String basePath = databaseUtil.getBasePath();
        String backupPath = databaseUtil.getBackupPath(winBackupPath, linuxBackupPath);
        String fullPath = basePath + backupPath;
        String dbName = databaseUtil.getDbName(dbUrl);

        String tmpBackupPath = "/tmp/" + backupFileName;

        String[] copyCmd = {
                "cmd.exe", "/c",
                "docker cp " + fullPath + backupFileName + " madeg_postgres:" + tmpBackupPath
        };
        String[] restoreCmd = {
                "cmd.exe", "/c",
                "docker exec -i madeg_postgres psql -U " + userName + " -d " + dbName + " -f " + tmpBackupPath
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
