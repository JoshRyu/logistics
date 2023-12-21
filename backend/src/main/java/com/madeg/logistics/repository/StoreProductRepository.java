package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreProductRepository
  extends CrudRepository<StoreProduct, Long> {
  StoreProduct findByStoreAndProduct(Store store, Product product);

  @Query(
    "SELECT sp FROM StoreProduct sp WHERE sp.store.storeCode = :storeCode ORDER BY sp.product.productCode ASC"
  )
  Page<StoreProduct> findByStoreCode(String storeCode, Pageable pageable);
}
