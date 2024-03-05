package com.madeg.logistics.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madeg.logistics.domain.SimplePageInfo;
import com.madeg.logistics.domain.StoreStatisticsRes;
import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreStatistics;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.repository.StoreStatisticsRepository;
import com.madeg.logistics.service.StoreStatisticsService;
import com.madeg.logistics.util.CommonUtil;

@Service
public class StoreStatisticsSercieImpl implements StoreStatisticsService {

    private StoreProductRepository storeProductRepository;
    private SalesHistoryRepository salesHistoryRepository;
    private StoreStatisticsRepository storeStatisticsRepository;
    private CommonUtil commonUtil;

    public StoreStatisticsSercieImpl(StoreProductRepository storeProductRepository,
            SalesHistoryRepository salesHistoryRepository, StoreStatisticsRepository storeStatisticsRepository,
            CommonUtil commonUtil) {
        this.storeProductRepository = storeProductRepository;
        this.salesHistoryRepository = salesHistoryRepository;
        this.storeStatisticsRepository = storeStatisticsRepository;
        this.commonUtil = commonUtil;
    }

    @Override
    @Transactional
    public void calculateStatistics(
            String storeCode,
            Integer year,
            Integer month) {
        String targetMonth = commonUtil.generateMonthFormat(year, month);
        Store store = commonUtil.findStoreByCode(storeCode);

        List<Long> storeProductIds = storeProductRepository.findStoreProductIdsByStore(
                storeCode);

        BigDecimal monthRevenue = BigDecimal.ZERO;
        for (Long id : storeProductIds) {
            SalesHistory productRevenue = salesHistoryRepository.findByStoreProduct_StoreProductIdAndSalesMonth(
                    id,
                    targetMonth);
            BigDecimal salesPrice = productRevenue.getSalesPrice();
            BigDecimal quantity = BigDecimal.valueOf(productRevenue.getQuantity());

            monthRevenue = monthRevenue.add(salesPrice.multiply(quantity));
        }

        BigDecimal oneMinusCommissionRate = BigDecimal.ONE.subtract(
                BigDecimal.valueOf((double) store.getCommissionRate() / 100));
        BigDecimal revenueAfterCommission = monthRevenue.multiply(
                oneMinusCommissionRate);
        BigDecimal monthProfit = revenueAfterCommission.subtract(
                BigDecimal.valueOf(store.getFixedCost()));

        StoreStatistics existStoreStatistics = storeStatisticsRepository.findByStoreAndMonth(
                store,
                targetMonth);

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

    @Override
    public StoreStatisticsRes getStoreStatistics(
            String storeCode,
            Pageable pageable) {
        Store store = commonUtil.findStoreByCode(storeCode);

        Page<StoreStatistics> page = storeStatisticsRepository.findByStoreOrderByMonth(
                store,
                pageable);

        List<StoreStatistics> content = page.getContent();
        SimplePageInfo simplePageInfo = commonUtil.createSimplePageInfo(page);

        return new StoreStatisticsRes(
                ResponseCode.RETRIEVED.getCode(),
                ResponseCode.RETRIEVED.getMessage("매장 통계 목록"),
                content,
                simplePageInfo);
    }
}
