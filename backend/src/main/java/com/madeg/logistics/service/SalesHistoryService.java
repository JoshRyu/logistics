package com.madeg.logistics.service;

import com.madeg.logistics.domain.SalesHistoryInput;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    String month =
      salesHistoryInput.getSalesYear() +
      "-" +
      salesHistoryInput.getSalesMonth();

    SalesHistory existSalesHistory = salesHistoryRepository.findByStoreProduct(
      previousStoreProduct
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
}
