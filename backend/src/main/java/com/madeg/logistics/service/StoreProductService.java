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
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StoreProductService extends CommonService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private StoreProductRepository storeProductRepository;

  public void registerStoreProduct(
    String storeCode,
    String productCode,
    StoreProductInput storeProductInput
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);

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

    validateStock(
      product.getStock(),
      income,
      "STOCK CNT SHOULD BE SMALLER OR EQUALS TO NOT REGISTERED PRODUCT STOCK"
    );

    product.updateStock(product.getStock() - income);
    productRepository.save(product);

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
      .showFlag(true)
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

    SimplePageInfo simplePageInfo = createSimplePageInfo(page);

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
      previousStoreProduct.updateDescription(patchInput.getDescription());
      storeProductRepository.save(previousStoreProduct);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "STORE PRODUCT IS NOT UPDATED"
      );
    }
  }

  public void disableStoreProduct(String storeCode, String productCode) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct previousStoreProduct = findStoreProduct(store, product);

    previousStoreProduct.updateShowFlag(false);

    storeProductRepository.save(previousStoreProduct);
  }

  public void restockStoreProduct(
    String storeCode,
    String productCode,
    Integer restockCnt
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct previousStoreProduct = findStoreProduct(store, product);

    validateStock(
      product.getStock(),
      restockCnt,
      "RESTOCK CNT SHOULD BE SMALLER OR EQUALS TO NOT REGISTERED PRODUCT STOCK"
    );

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

  public void updateDefectedStoreProduct(
    String storeCode,
    String productCode,
    Integer defectCnt
  ) {
    Store store = findStoreByCode(storeCode);
    Product product = findProductByCode(productCode);
    StoreProduct previousStoreProduct = findStoreProduct(store, product);

    Integer currentStockCnt = previousStoreProduct.getStockCnt() == null
      ? 0
      : previousStoreProduct.getStockCnt();
    Integer currentDefectCnt = previousStoreProduct.getDefectCnt() == null
      ? 0
      : previousStoreProduct.getDefectCnt();

    previousStoreProduct.updateStockCnt(currentStockCnt - defectCnt);
    previousStoreProduct.updateDefectCnt(currentDefectCnt + defectCnt);

    storeProductRepository.save(previousStoreProduct);
  }
}
