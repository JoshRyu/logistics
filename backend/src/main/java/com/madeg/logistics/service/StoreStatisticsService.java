package com.madeg.logistics.service;

import org.springframework.data.domain.Pageable;

import com.madeg.logistics.domain.StoreStatisticsRes;

public interface StoreStatisticsService {

  void calculateStatistics(String storeCode, Integer year, Integer month);

  StoreStatisticsRes getStoreStatistics(String storeCode, Pageable pageable);

}