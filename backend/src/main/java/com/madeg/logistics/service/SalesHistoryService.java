package com.madeg.logistics.service;

import com.madeg.logistics.domain.SalesHistoryInput;
import com.madeg.logistics.domain.SalesHistoryPatch;
import com.madeg.logistics.domain.SalesHistoryRes;

import org.springframework.data.domain.Pageable;

public interface SalesHistoryService {

  void registerSalesHistory(String storeCode, String productCode, SalesHistoryInput salesHistoryInput);

  SalesHistoryRes getSalesHistoryByStoreAndProduct(String storeCode, String productCode, Pageable pageable);

  SalesHistoryRes getSalesHistoryByStoreAndSalesMonth(String storeCode, Integer salesYear, Integer salesMonth,
      Pageable pageable);

  void patchSalesHistory(String storeCode, String productCode, SalesHistoryPatch patchInput);

}