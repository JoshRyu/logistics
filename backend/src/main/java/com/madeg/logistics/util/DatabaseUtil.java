package com.madeg.logistics.util;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DatabaseUtil {

    public String getFullBackupPath(String winBackupPath, String linuxBackupPath) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) {
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            if (currentDir.endsWith("backend"))
                currentDir = currentDir.getParent();
            return currentDir.toString() + winBackupPath;
        } else {
            return linuxBackupPath;
        }
    }

    public String getDbName(String dbUrl) {
        return dbUrl.substring(dbUrl.lastIndexOf('/') + 1);
    }
}
