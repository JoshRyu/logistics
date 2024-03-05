package com.madeg.logistics.service;

import com.madeg.logistics.domain.StoreProductInput;
import com.madeg.logistics.domain.StoreProductPatch;
import com.madeg.logistics.domain.StoreProductRes;

import org.springframework.data.domain.Pageable;

public interface StoreProductService {

  void registerStoreProduct(String storeCode, String productCode, StoreProductInput storeProductInput);

  StoreProductRes getStoreProducts(String storeCode, Pageable pageable);

  void patchStoreProduct(String storeCode, String productCode, StoreProductPatch patchInput);

  void disableStoreProduct(String storeCode, String productCode);

  void restockStoreProduct(String storeCode, String productCode, Integer restockCnt);

  void updateDefectedStoreProduct(String storeCode, String productCode, Integer defectCnt);

}