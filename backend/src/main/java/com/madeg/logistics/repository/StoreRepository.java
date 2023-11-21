package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Store;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends CrudRepository<Store, String> {
  Store findByName(String name);
  List<Store> findAll();
}
