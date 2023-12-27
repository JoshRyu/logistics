package com.madeg.logistics.service;

import com.madeg.logistics.domain.SalesHistoryInput;
import com.madeg.logistics.domain.SalesHistoryPatch;
import com.madeg.logistics.domain.SalesHistoryRes;
import com.madeg.logistics.domain.SimplePageInfo;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SalesHistoryService extends CommonService {

  @Autowired
  private StoreProductRepository storeProductRepository;

  @Autowired
  private SalesHistoryRepository salesHistoryRepository;

  public void registerSalesHistory(
    String storeCode,
    String productCode,
    SalesHistoryInput salesHistoryInput
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct previousStoreProduct = findStoreProduct(store, product);

    String month = generateMonthFormat(
      salesHistoryInput.getSalesYear(),
      salesHistoryInput.getSalesMonth()
    );

    SalesHistory existSalesHistory = salesHistoryRepository.findByStoreProductAndSalesMonth(
      previousStoreProduct,
      month
    );

    if (existSalesHistory != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "SALES HISTORY IS ALREADY REGISTERED"
      );
    }

    previousStoreProduct.updateStockCnt(
      previousStoreProduct.getStockCnt() - salesHistoryInput.getQuantity()
    );

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
    Pageable pageable
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct storeProduct = findStoreProduct(store, product);

    Page<SalesHistory> page = salesHistoryRepository.findByStoreProductOrderBySalesMonth(
      storeProduct,
      pageable
    );

    List<SalesHistory> content = page.getContent();
    SimplePageInfo simplePageInfo = createSimplePageInfo(page);

    return new SalesHistoryRes(
      HttpStatus.OK.value(),
      "STORE PRODUCT SALES HISTORY IS RETRIEVED",
      content,
      simplePageInfo
    );
  }

  public SalesHistoryRes getSalesHistoryByStoreAndSalesMonth(
    String storeCode,
    Integer salesYear,
    Integer salesMonth,
    Pageable pageable
  ) {
    Store store = findStoreByCode(storeCode);
    StoreProduct storeProduct = storeProductRepository.findByStore(store);

    if (storeProduct == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE PRODUCT NOT FOUND"
      );
    }

    Page<SalesHistory> page = salesHistoryRepository.findByStoreProductAndSalesMonth(
      storeProduct,
      generateMonthFormat(salesYear, salesMonth),
      pageable
    );

    List<SalesHistory> content = page.getContent();
    SimplePageInfo simplePageInfo = createSimplePageInfo(page);

    return new SalesHistoryRes(
      HttpStatus.OK.value(),
      "STORE SALES HISTORY IN SPECIFIC MONTH IS RETRIEVED",
      content,
      simplePageInfo
    );
  }

  public void patchSalesHistory(
    String storeCode,
    String productCode,
    SalesHistoryPatch patchInput
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct storeProduct = findStoreProduct(store, product);
    SalesHistory previousSalesHistory = findSalesHistory(
      storeProduct,
      generateMonthFormat(patchInput.getSalesYear(), patchInput.getSalesMonth())
    );

    validateStock(
      storeProduct.getStockCnt(),
      (patchInput.getQuantity() - previousSalesHistory.getQuantity()),
      "ADDITIONAL QUANTITY SHOULD BE SMALLER OR EQUALS TO STROE PRODUCT STOCK CNT"
    );

    if (previousSalesHistory.isStateChanged(patchInput)) {
      if (patchInput.getQuantity() != null) {
        storeProduct.updateStockCnt(
          storeProduct.getStockCnt() -
          (patchInput.getQuantity() - previousSalesHistory.getQuantity())
        );
        previousSalesHistory.updateQuantity(patchInput.getQuantity());
      }

      previousSalesHistory.updateMemo(patchInput.getMemo());

      storeProductRepository.save(storeProduct);
      salesHistoryRepository.save(previousSalesHistory);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "SALES HISTORY IS NOT UPDATED"
      );
    }
  }
}
