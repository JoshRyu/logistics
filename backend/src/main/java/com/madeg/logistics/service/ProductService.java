package com.madeg.logistics.service;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.domain.ProductListReq;
import com.madeg.logistics.domain.ProductPatch;
import com.madeg.logistics.domain.ProductRes;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.enums.ProductType;

import org.springframework.data.domain.Pageable;

public interface ProductService {

  void createProduct(ProductInput productInput);

  ProductRes getProducts(ProductType type, ProductListReq productListReq, Pageable pageable);

  Product getProductByCode(String productCode);

  void patchProduct(String productCode, ProductPatch patchInput);

  void deleteProduct(String productCode);

}