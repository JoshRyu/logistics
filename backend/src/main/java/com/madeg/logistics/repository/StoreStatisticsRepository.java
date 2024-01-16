package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreStatisticsRepository
    extends JpaRepository<StoreStatistics, Long> {
  StoreStatistics findByStoreAndMonth(Store store, String month);

  Page<StoreStatistics> findByStoreOrderByMonth(Store store, Pageable pageable);
}
