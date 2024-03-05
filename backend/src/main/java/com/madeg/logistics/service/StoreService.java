package com.madeg.logistics.service;

import com.madeg.logistics.domain.StoreInput;
import com.madeg.logistics.domain.StorePatch;
import com.madeg.logistics.entity.Store;

import java.util.List;

public interface StoreService {

  void createStore(StoreInput storeInput);

  List<Store> getStores();

  void patchStore(String storeCode, StorePatch patchInput);

  void deleteStore(String storeCode);

}