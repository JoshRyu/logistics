package com.madeg.logistics.service;

import com.madeg.logistics.domain.CommonRes;

public interface RestoreService {

    CommonRes restoreDatabase(String backupFileName);

}