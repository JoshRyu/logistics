package com.madeg.logistics.repository;

import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.StoreProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesHistoryRepository
  extends CrudRepository<SalesHistory, Long> {
  SalesHistory findByStoreProductAndSalesMonth(
    StoreProduct storeProduct,
    String salesMonth
  );

  SalesHistory findByStoreProduct_StoreProductIdAndSalesMonth(
    Long storeProductId,
    String salesMonth
  );

  Page<SalesHistory> findByStoreProductOrderBySalesMonth(
    StoreProduct storeProduct,
    Pageable pageable
  );

  Page<SalesHistory> findByStoreProductAndSalesMonth(
    StoreProduct storeProduct,
    String salesMonth,
    Pageable pageable
  );
}
