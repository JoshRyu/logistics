package com.madeg.logistics.service;

import com.madeg.logistics.domain.SalesHistoryInput;
import com.madeg.logistics.domain.SalesHistoryPatch;
import com.madeg.logistics.domain.SalesHistoryRes;
import com.madeg.logistics.domain.SimplePageInfo;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.util.CommonUtil;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SalesHistoryService {

  private StoreProductRepository storeProductRepository;
  private SalesHistoryRepository salesHistoryRepository;
  private CommonUtil commonUtil;

  public SalesHistoryService(StoreProductRepository storeProductRepository,
      SalesHistoryRepository salesHistoryRepository, CommonUtil commonUtil) {
    this.storeProductRepository = storeProductRepository;
    this.salesHistoryRepository = salesHistoryRepository;
    this.commonUtil = commonUtil;
  }

  public void registerSalesHistory(
      String storeCode,
      String productCode,
      SalesHistoryInput salesHistoryInput) {
    Store store = commonUtil.findStoreByCode(storeCode);
    Product product = commonUtil.findProductByCode(productCode);
    StoreProduct previousStoreProduct = commonUtil.findStoreProduct(store, product);

    String month = commonUtil.generateMonthFormat(
        salesHistoryInput.getSalesYear(),
        salesHistoryInput.getSalesMonth());

    SalesHistory existSalesHistory = salesHistoryRepository.findByStoreProductAndSalesMonth(
        previousStoreProduct,
        month);

    if (existSalesHistory != null) {
      throw new ResponseStatusException(
          ResponseCode.CONFLICT.getStatus(),
          ResponseCode.CONFLICT.getMessage("판매 이력"));
    }

    previousStoreProduct.updateStockCnt(
        previousStoreProduct.getStockCnt() - salesHistoryInput.getQuantity());

    storeProductRepository.save(previousStoreProduct);

    SalesHistory salesHistory = SalesHistory
        .builder()
        .storeProduct(previousStoreProduct)
        .salesMonth(month)
        .quantity(salesHistoryInput.getQuantity())
        .salesPrice(previousStoreProduct.getStorePrice())
        .memo(salesHistoryInput.getMemo())
        .build();

    salesHistoryRepository.save(salesHistory);
  }

  public SalesHistoryRes getSalesHistoryByStoreAndProduct(
      String storeCode,
      String productCode,
      Pageable pageable) {
    Store store = commonUtil.findStoreByCode(storeCode);
    Product product = commonUtil.findProductByCode(productCode);
    StoreProduct storeProduct = commonUtil.findStoreProduct(store, product);

    Page<SalesHistory> page = salesHistoryRepository.findByStoreProductOrderBySalesMonth(
        storeProduct,
        pageable);

    List<SalesHistory> content = page.getContent();
    SimplePageInfo simplePageInfo = commonUtil.createSimplePageInfo(page);

    return new SalesHistoryRes(
        ResponseCode.RETRIEVED.getCode(),
        ResponseCode.RETRIEVED.getMessage("매장 제품 판매 이력 목록"),
        content,
        simplePageInfo);
  }

  public SalesHistoryRes getSalesHistoryByStoreAndSalesMonth(
      String storeCode,
      Integer salesYear,
      Integer salesMonth,
      Pageable pageable) {
    Store store = commonUtil.findStoreByCode(storeCode);
    StoreProduct storeProduct = storeProductRepository.findByStore(store);

    if (storeProduct == null) {
      throw new ResponseStatusException(
          ResponseCode.NOT_FOUND.getStatus(),
          ResponseCode.NOT_FOUND.getMessage("매장 제품"));
    }

    Page<SalesHistory> page = salesHistoryRepository.findByStoreProductAndSalesMonth(
        storeProduct,
        commonUtil.generateMonthFormat(salesYear, salesMonth),
        pageable);

    List<SalesHistory> content = page.getContent();
    SimplePageInfo simplePageInfo = commonUtil.createSimplePageInfo(page);

    return new SalesHistoryRes(
        ResponseCode.RETRIEVED.getCode(),
        ResponseCode.RETRIEVED.getMessage("월별 매장 판매 이력 목록"),
        content,
        simplePageInfo);
  }

  public void patchSalesHistory(
      String storeCode,
      String productCode,
      SalesHistoryPatch patchInput) {
    Store store = commonUtil.findStoreByCode(storeCode);
    Product product = commonUtil.findProductByCode(productCode);
    StoreProduct storeProduct = commonUtil.findStoreProduct(store, product);
    SalesHistory previousSalesHistory = commonUtil.findSalesHistory(
        storeProduct,
        commonUtil.generateMonthFormat(patchInput.getSalesYear(), patchInput.getSalesMonth()));

    commonUtil.validateStock(
        storeProduct.getStockCnt(),
        (patchInput.getQuantity() - previousSalesHistory.getQuantity()),
        "추가 수량은 매장 제품 재고 수량보다 작거나 같아야 합니다");

    if (previousSalesHistory.isStateChanged(patchInput)) {
      if (patchInput.getQuantity() != null) {
        storeProduct.updateStockCnt(
            storeProduct.getStockCnt() -
                (patchInput.getQuantity() - previousSalesHistory.getQuantity()));
        previousSalesHistory.updateQuantity(patchInput.getQuantity());
      }

      previousSalesHistory.updateMemo(patchInput.getMemo());

      storeProductRepository.save(storeProduct);
      salesHistoryRepository.save(previousSalesHistory);
    } else {
      throw new ResponseStatusException(
          ResponseCode.UNCHANGED.getStatus(),
          ResponseCode.UNCHANGED.getMessage("판매 이력"));
    }
  }
}
