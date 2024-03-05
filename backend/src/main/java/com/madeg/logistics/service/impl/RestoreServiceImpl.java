package com.madeg.logistics.service.impl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.service.RestoreService;
import com.madeg.logistics.util.DatabaseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestoreServiceImpl implements RestoreService {

    private final DatabaseUtil databaseUtil;
    private Environment env;
    private String dbUrl;
    private String userName;
    private String winBackupPath;
    private String linuxBackupPath;

    public RestoreServiceImpl(DatabaseUtil databaseUtil, Environment env) {
        this.databaseUtil = databaseUtil;
        this.env = env;
        initProperties();
    }

    private void initProperties() {
        this.dbUrl = env.getProperty("spring.datasource.url");
        this.userName = env.getProperty("spring.datasource.username");
        this.winBackupPath = env.getProperty("backup.path.win");
        this.linuxBackupPath = env.getProperty("backup.path.linux");
    }

    public CommonRes restoreDatabase(String backupFileName) {
        String dbName = databaseUtil.getDbName(dbUrl);
        String fullPath = databaseUtil.getFullBackupPath(winBackupPath, linuxBackupPath);
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        String[] winCmd = new String[] { "cmd.exe", "/c", "docker cp " + fullPath + backupFileName
                + " madeg_postgres:/tmp/" + backupFileName + " && " + "docker exec -i madeg_postgres psql -U "
                + userName + " -d " + dbName + " -f /tmp/"
                + backupFileName };

        String[] linuxCmd = new String[] { "/bin/bash", "-c",
                "sudo -u postgres psql -U " + userName + " -d " + dbName + " -f " + fullPath
                        + backupFileName };

        String[] cmd = isWindows ? winCmd : linuxCmd;
        ProcessBuilder pb = new ProcessBuilder(cmd);
        try {
            Process process = pb.start();
            boolean finish = process.waitFor(60, TimeUnit.SECONDS);

            if (finish && process.exitValue() == 0) {
                log.info("Database restored successfully from " + fullPath);
                return new CommonRes(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage("DB 복구"));
            } else {
                log.error("Error occurred during database restoration or timeout reached.");
                return new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage());
            }
        } catch (InterruptedException | IOException e) {
            log.error("Exception occurred during restore process", e);
            return new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage());
        }
    }

}
