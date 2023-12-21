package com.madeg.logistics.service;

import com.madeg.logistics.domain.StoreProductInput;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.entity.Store;
import com.madeg.logistics.entity.StoreProduct;
import com.madeg.logistics.repository.ProductRepository;
import com.madeg.logistics.repository.StoreProductRepository;
import com.madeg.logistics.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  public void registerStoreProduct(StoreProductInput storeProductInput) {
    Store store = storeRepository.findByStoreCode(
      storeProductInput.getStoreCode()
    );

    if (store == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "STORE NOT FOUND"
      );
    }

    Product product = productRepository.findByProductCode(
      storeProductInput.getProductCode()
    );

    if (product == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "PRODUCT NOT FOUND"
      );
    }

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
}
