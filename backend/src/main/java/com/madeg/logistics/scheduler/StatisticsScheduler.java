package com.madeg.logistics.scheduler;

import com.madeg.logistics.entity.Store;
import com.madeg.logistics.repository.StoreRepository;
import com.madeg.logistics.service.StoreStatisticsService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticsScheduler {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private StoreStatisticsService storeStatisticsService;

  //   @Scheduled(cron = "0 * * * * ?")  // For testing purposes, it is configured to run every minute.
  @Scheduled(cron = "0 0 0 L * ?") // Execute at midnight on the last day of every month
  public void scheduleMonthlyStatisticsCalculation() {
    LocalDate today = LocalDate.now();
    int year = today.getYear();
    int month = today.getMonthValue();

    List<Store> stores = storeRepository.findAll();
    for (Store store : stores) {
      storeStatisticsService.calculateStatistics(
        store.getStoreCode(),
        year,
        month
      );
    }
  }
}
