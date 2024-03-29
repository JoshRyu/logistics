package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreProductRepository
    extends JpaRepository<StoreProduct, Long> {
  StoreProduct findByStore(Store store);

  StoreProduct findByStoreAndProduct(Store store, Product product);

  @Query("SELECT sp.storeProductId FROM StoreProduct sp WHERE sp.store.storeCode = :storeCode")
  List<Long> findStoreProductIdsByStore(String storeCode);

  @Query("SELECT sp FROM StoreProduct sp WHERE sp.store.storeCode = :storeCode ORDER BY sp.product.productCode ASC")
  Page<StoreProduct> findByStoreCode(String storeCode, Pageable pageable);
}
