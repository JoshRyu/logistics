package com.madeg.logistics.service;

import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreStatistics;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.repository.StoreStatisticsRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreStatisticsService extends CommonService {

  @Autowired
  private StoreProductRepository storeProductRepository;

  @Autowired
  private SalesHistoryRepository salesHistoryRepository;

  @Autowired
  private StoreStatisticsRepository storeStatisticsRepository;

  public void calculateStatistics(
    String storeCode,
    Integer year,
    Integer month
  ) {
    String targetMonth = generateMonthFormat(year, month);
    Store store = findStoreByCode(storeCode);

    List<Long> storeProductIds = storeProductRepository.findStoreProductIdsByStore(
      storeCode
    );

    BigDecimal monthRevenue = BigDecimal.ZERO;
    for (Long id : storeProductIds) {
      SalesHistory productRevenue = salesHistoryRepository.findByStoreProduct_StoreProductIdAndSalesMonth(
        id,
        targetMonth
      );
      BigDecimal salesPrice = productRevenue.getSalesPrice();
      BigDecimal quantity = BigDecimal.valueOf(productRevenue.getQuantity());

      monthRevenue = monthRevenue.add(salesPrice.multiply(quantity));
    }

    BigDecimal oneMinusCommissionRate = BigDecimal.ONE.subtract(
      BigDecimal.valueOf((double) store.getCommissionRate() / 100)
    );
    BigDecimal revenueAfterCommission = monthRevenue.multiply(
      oneMinusCommissionRate
    );
    BigDecimal monthProfit = revenueAfterCommission.subtract(
      BigDecimal.valueOf(store.getFixedCost())
    );

    StoreStatistics existStoreStatistics = storeStatisticsRepository.findByStoreAndMonth(
      store,
      targetMonth
    );

    if (existStoreStatistics != null) {
      existStoreStatistics.updateMonthRevenue(monthRevenue);
      existStoreStatistics.updateMonthProfit(monthProfit);
      existStoreStatistics.updateLastUpdatedTime(LocalDateTime.now());
      storeStatisticsRepository.save(existStoreStatistics);
    } else {
      StoreStatistics storeStatistics = StoreStatistics
        .builder()
        .month(targetMonth)
        .store(store)
        .lastUpdatedTime(LocalDateTime.now())
        .monthRevenue(monthRevenue)
        .monthProfit(monthProfit)
        .build();

      storeStatisticsRepository.save(storeStatistics);
    }
  }
}
