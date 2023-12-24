package com.madeg.logistics.service;

import com.madeg.logistics.domain.SimplePageInfo;
import com.madeg.logistics.domain.StoreProductInput;
import com.madeg.logistics.domain.StoreProductOutput;
import com.madeg.logistics.domain.StoreProductPatch;
import com.madeg.logistics.domain.StoreProductRes;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.repository.ProductRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StoreProductService {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private StoreProductRepository storeProductRepository;

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

  public void registerStoreProduct(
    String storeCode,
    StoreProductInput storeProductInput
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(storeProductInput.getProductCode());

    StoreProduct existStoreProduct = storeProductRepository.findByStoreAndProduct(
      store,
      product
    );

    if (existStoreProduct != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        String.format(
          "PRODUCT %s IS ALREADY REGISTERED IN STORE %s",
          product.getName(),
          store.getName()
        )
      );
    }

    Integer income = storeProductInput.getIncomeCnt() == null
      ? 0
      : storeProductInput.getIncomeCnt();

    StoreProduct storeProduct = StoreProduct
      .builder()
      .store(store)
      .product(product)
      .storePrice(
        storeProductInput.getStorePrice() == null
          ? product.getPrice()
          : storeProductInput.getStorePrice()
      )
      .incomeCnt(income)
      .stockCnt(income)
      .defectCnt(0)
      .description(storeProductInput.getDescription())
      .build();

    storeProductRepository.save(storeProduct);
  }

  public StoreProductRes getStoreProducts(String storeCode, Pageable pageable) {
    Page<StoreProduct> page = storeProductRepository.findByStoreCode(
      storeCode,
      pageable
    );

    List<StoreProductOutput> content = new ArrayList<>();

    for (StoreProduct sp : page.getContent()) {
      StoreProductOutput output = new StoreProductOutput(
        sp.getProduct().getProductCode(),
        sp.getProduct().getName(),
        sp.getStorePrice(),
        sp.getIncomeCnt(),
        sp.getDefectCnt(),
        sp.getDescription()
      );

      content.add(output);
    }

    SimplePageInfo simplePageInfo = new SimplePageInfo();
    simplePageInfo.setLast(page.isLast());
    simplePageInfo.setPage(page.getNumber());
    simplePageInfo.setSize(page.getSize());
    simplePageInfo.setTotalPages(page.getTotalPages());
    simplePageInfo.setTotalElements(page.getTotalElements());

    return new StoreProductRes(
      HttpStatus.OK.value(),
      "STORE'S PRODUCTS ARE RETRIEVED",
      content,
      simplePageInfo
    );
  }

  public void patchStoreProduct(
    String storeCode,
    String productCode,
    StoreProductPatch patchInput
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct previousStoreProduct = findStoreProduct(store, product);

    if (previousStoreProduct.isStateChanged((patchInput))) {
      if (patchInput.getStorePrice() != null) {
        previousStoreProduct.updateStorePrice(patchInput.getStorePrice());
      } else {
        previousStoreProduct.updateStorePrice(product.getPrice());
      }

      if (patchInput.getDescription() != null) {
        previousStoreProduct.updateDescription(patchInput.getDescription());
      }

      storeProductRepository.save(previousStoreProduct);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "STORE PRODUCT IS NOT UPDATED"
      );
    }
  }

  public void deleteStoreProduct(String storeCode, String productCode) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct previousStoreProduct = findStoreProduct(store, product);

    storeProductRepository.delete(previousStoreProduct);
  }

  public void restockStoreProduct(
    String storeCode,
    String productCode,
    Integer restockCnt
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct previousStoreProduct = findStoreProduct(store, product);

    product.updateStock(product.getStock() - restockCnt);

    productRepository.save(product);

    Integer currentIncomeCnt = previousStoreProduct.getIncomeCnt() == null
      ? 0
      : previousStoreProduct.getIncomeCnt();
    Integer currentStockCnt = previousStoreProduct.getStockCnt() == null
      ? 0
      : previousStoreProduct.getStockCnt();

    previousStoreProduct.updateIncomeCnt(currentIncomeCnt + restockCnt);
    previousStoreProduct.updateStockCnt(currentStockCnt + restockCnt);

    storeProductRepository.save(previousStoreProduct);
  }
}
