package com.madeg.logistics.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.madeg.logistics.util.DatabaseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

    public void restoreDatabase(String backupFileName) {
        String dbName = databaseUtil.getDbName(dbUrl);
        String fullPath = databaseUtil.getFullBackupPath(winBackupPath, linuxBackupPath);
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        String[] cmd;
        if (isWindows) {
            String dockerCopyCmd = "docker cp " + fullPath + backupFileName + " madeg_postgres:/tmp/" + backupFileName;
            String dockerRestoreCmd = "docker exec -i madeg_postgres psql -U " + userName + " -d " + dbName
                    + " -f /tmp/" + backupFileName;
            cmd = new String[] { "cmd.exe", "/c", dockerCopyCmd + " && " + dockerRestoreCmd };
        } else {
            String restoreCmd = "sudo -u postgres psql -U " + userName + " -d " + dbName + " -f " + fullPath
                    + backupFileName;
            cmd = new String[] { "/bin/bash", "-c", restoreCmd };
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process process = pb.start();
            boolean finish = process.waitFor(60, TimeUnit.SECONDS);

            if (finish && process.exitValue() == 0) {
                log.info("Database restored successfully from " + fullPath);
            } else {
                log.error("Error occurred during database restoration or timeout reached.");
            }
        } catch (InterruptedException | IOException e) {
            log.error("Exception occurred during restore process", e);
        }
    }

}
