package com.madeg.logistics.util;

import org.springframework.stereotype.Component;
import java.nio.file.Paths;

@Component
public class DatabaseUtil {
    public String getBasePath() {
        return Paths.get(System.getProperty("user.dir")).toString();
    }

    public String getBackupPath(String winBackupPath, String linuxBackupPath) {
        return System.getProperty("os.name").toLowerCase().contains("win") ? winBackupPath : linuxBackupPath;
    }

    public String getDbName(String dbUrl) {
        return dbUrl.substring(dbUrl.lastIndexOf('/') + 1);
    }
}
