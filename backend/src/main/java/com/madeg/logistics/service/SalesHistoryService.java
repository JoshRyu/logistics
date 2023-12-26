package com.madeg.logistics.service;

import com.madeg.logistics.domain.SalesHistoryInput;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.repository.ProductRepository;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SalesHistoryService {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private StoreProductRepository storeProductRepository;

  @Autowired
  private SalesHistoryRepository salesHistoryRepository;

  private Store findStoreByCode(String storeCode) {
    Store store = storeRepository.findByStoreCode(storeCode);
    if (store == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE NOT FOUND"
      );
    }
    return store;
  }

  private Product findProductByCode(String productCode) {
    Product product = productRepository.findByProductCode(productCode);
    if (product == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "PRODUCT NOT FOUND"
      );
    }
    return product;
  }

  private StoreProduct findStoreProduct(Store store, Product product) {
    StoreProduct previousStoreProduct = storeProductRepository.findByStoreAndProduct(
      store,
      product
    );

    if (previousStoreProduct == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE PRODUCT NOT FOUND"
      );
    }
    return previousStoreProduct;
  }

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
