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
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("win");

        String[] cmd;
        if (isWindows) {
            String dockerCopyCmd = "docker cp " + fullPath + backupFileName + " madeg_postgres:/tmp/" + backupFileName;
            String dockerRestoreCmd = "docker exec -i madeg_postgres psql -U " + userName + " -d " + dbName
                    + " -f /tmp/" + backupFileName;
            cmd = new String[] { "cmd.exe", "/c", dockerCopyCmd + " && " + dockerRestoreCmd };
        } else {
            String restoreCmd = "sudo -u postgres psql -U " + userName + " -d " + dbName + " -f " + backupPath
                    + backupFileName;
            cmd = new String[] { "/bin/bash", "-c", restoreCmd };
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process process = pb.start();
            boolean finish = process.waitFor(60, TimeUnit.SECONDS);

            if (finish && process.exitValue() == 0) {
                logger.info("Database restored successfully from " + backupPath);
            } else {
                logger.error("Error occurred during database restoration or timeout reached.");
            }
        } catch (InterruptedException | IOException e) {
            logger.error("Exception occurred during restore process", e);
        }
    }

}
