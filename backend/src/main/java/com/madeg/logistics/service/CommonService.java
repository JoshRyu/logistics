package com.madeg.logistics.service;

import com.madeg.logistics.domain.SimplePageInfo;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.SalesHistory;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.repository.CategoryRepository;
import com.madeg.logistics.repository.ProductRepository;
import com.madeg.logistics.repository.SalesHistoryRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.repository.StoreRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    if (category == null)
      throwNotFound("카테고리");

    return category;
  }

  protected Store findStoreByCode(String storeCode) {
    Store store = storeRepository.findByStoreCode(storeCode);
    if (store == null)
      throwNotFound("매장");

    return store;
  }

  protected Product findProductByCode(String productCode) {
    Product product = productRepository.findByProductCode(productCode);
    if (product == null)
      throwNotFound("제품");

    return product;
  }

  protected StoreProduct findStoreProduct(Store store, Product product) {
    StoreProduct storeProduct = storeProductRepository.findByStoreAndProduct(
        store,
        product);

    if (storeProduct == null)
      throwNotFound("매장 제품");

    return storeProduct;
  }

  protected SalesHistory findSalesHistory(
      StoreProduct storeProduct,
      String salesMonth) {
    SalesHistory salesHistory = salesHistoryRepository.findByStoreProductAndSalesMonth(
        storeProduct,
        salesMonth);

    if (salesHistory == null)
      throwNotFound("판매 이력");

    return salesHistory;
  }

  private void throwNotFound(String entityType) {
    throw new ResponseStatusException(
        ResponseCode.NOTFOUND.getStatus(),
        ResponseCode.NOTFOUND.getMessage(entityType));
  }

  protected byte[] getImageBytes(MultipartFile image) {
    if (image == null) {
      return null;
    }
    try {
      return image.getBytes();
    } catch (IOException e) {
      throw new ResponseStatusException(
          ResponseCode.BADREQUEST.getStatus(),
          ResponseCode.BADREQUEST.getMessage("잘못된 이미지 입력입니다"));
    }
  }

  protected String generateMonthFormat(Integer year, Integer month) {
    return String.format("%d-%02d", year, month);
  }

  protected void validateStock(
      Integer stock1,
      Integer stock2,
      String errorMsg) {
    if (stock1 < stock2) {
      throw new ResponseStatusException(ResponseCode.BADREQUEST.getStatus(),
          ResponseCode.BADREQUEST.getMessage(errorMsg));
    }
  }

  protected <T> SimplePageInfo createSimplePageInfo(Page<T> page) {
    SimplePageInfo simplePageInfo = new SimplePageInfo();
    simplePageInfo.setLast(page.isLast());
    simplePageInfo.setPage(page.getNumber());
    simplePageInfo.setSize(page.getSize());
    simplePageInfo.setTotalPages(page.getTotalPages());
    simplePageInfo.setTotalElements(page.getTotalElements());
    return simplePageInfo;
  }
}
