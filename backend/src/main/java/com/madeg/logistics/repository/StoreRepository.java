package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Store;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
  Store findByName(String name);

  Store findByStoreCode(String storeCode);

  List<Store> findAll();
}
