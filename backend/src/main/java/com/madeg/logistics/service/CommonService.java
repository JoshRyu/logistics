package com.madeg.logistics.service;

import com.madeg.logistics.entity.Category;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.repository.CategoryRepository;
import com.madeg.logistics.repository.ProductRepository;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommonService {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private StoreProductRepository storeProductRepository;

  @Autowired
  private SalesHistoryRepository salesHistoryRepository;

  protected Category findCategoryByCode(String categoryCode) {
    Category category = categoryRepository.findByCategoryCode(categoryCode);

    if (category == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "CATEGORY NOT FOUND"
      );
    }

    return category;
  }

  protected Store findStoreByCode(String storeCode) {
    Store store = storeRepository.findByStoreCode(storeCode);
    if (store == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE NOT FOUND"
      );
    }
    return store;
  }

  protected Product findProductByCode(String productCode) {
    Product product = productRepository.findByProductCode(productCode);
    if (product == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "PRODUCT NOT FOUND"
      );
    }
    return product;
  }

  protected StoreProduct findStoreProduct(Store store, Product product) {
    StoreProduct storeProduct = storeProductRepository.findByStoreAndProduct(
      store,
      product
    );

    if (storeProduct == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE PRODUCT NOT FOUND"
      );
    }
    return storeProduct;
  }

  protected SalesHistory findSalesHistory(
    StoreProduct storeProduct,
    String salesMonth
  ) {
    SalesHistory salesHistory = salesHistoryRepository.findByStoreProductAndSalesMonth(
      storeProduct,
      salesMonth
    );

    if (salesHistory == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "SALES HISTORY NOT FOUND"
      );
    }

    return salesHistory;
  }

  protected String generateMonthFormat(Integer year, Integer month) {
    return String.format("%d-%02d", year, month);
  }
}
