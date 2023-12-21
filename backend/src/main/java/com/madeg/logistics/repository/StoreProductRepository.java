package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreProductRepository
  extends CrudRepository<StoreProduct, Long> {
  StoreProduct findByStoreAndProduct(Store store, Product product);
}
