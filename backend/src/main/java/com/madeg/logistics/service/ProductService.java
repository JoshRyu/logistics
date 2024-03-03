package com.madeg.logistics.service;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.domain.ProductListReq;
import com.madeg.logistics.domain.ProductPatch;
import com.madeg.logistics.domain.ProductRes;
import com.madeg.logistics.domain.ProductSpecification;
import com.madeg.logistics.domain.SimplePageInfo;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.enums.ProductType;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.repository.CategoryRepository;
import com.madeg.logistics.repository.ProductRepository;
import com.madeg.logistics.util.CommonUtil;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

  private ProductRepository productRepository;
  private CategoryRepository categoryRepository;
  private CommonUtil commonUtil;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
      CommonUtil commonUtil) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.commonUtil = commonUtil;

  }

  public void createProduct(ProductInput productInput) {
    Product existProduct = productRepository.findByName(productInput.getName());

    if (existProduct != null) {
      throw new ResponseStatusException(
          ResponseCode.CONFLICT.getStatus(),
          ResponseCode.CONFLICT.getMessage("제품"));
    }

    Category existCategory = categoryRepository.findByCategoryCode(
        productInput.getCategoryCode());

    if (existCategory == null) {
      throw new ResponseStatusException(
          ResponseCode.NOT_FOUND.getStatus(),
          ResponseCode.NOT_FOUND.getMessage("카테고리"));
    }

    byte[] imageBytes = commonUtil.getImageBytes(productInput.getImg());

    Product product = Product
        .builder()
        .category(existCategory)
        .name(productInput.getName())
        .price(productInput.getPrice())
        .stock(productInput.getStock())
        .type(productInput.getType())
        .img(imageBytes)
        .barcode(productInput.getBarcode())
        .description(productInput.getDescription())
        .build();

    productRepository.save(product);
  }

  public ProductRes getProducts(ProductType type, ProductListReq productListReq, Pageable pageable) {
    Specification<Product> spec = ProductSpecification.withDynamicQuery(type, productListReq);
    Page<Product> page = productRepository.findAll(spec, pageable);
    List<Product> content = page.getContent();

    SimplePageInfo simplePageInfo = commonUtil.createSimplePageInfo(page);

    return new ProductRes(
        ResponseCode.RETRIEVED.getCode(),
        ResponseCode.RETRIEVED.getMessage("제품 목록"),
        content,
        simplePageInfo);
  }

  public Product getProductByCode(String productCode) {
    return commonUtil.findProductByCode(productCode);
  }

  public void patchProduct(String productCode, ProductPatch patchInput) {
    Product previousProduct = commonUtil.findProductByCode(productCode);

    byte[] newImgBytes = commonUtil.getImageBytes(patchInput.getImg());

    if (previousProduct.isStateChanged(patchInput, newImgBytes)) {
      if (patchInput.getName() != null)
        previousProduct.updateName(
            patchInput.getName());
      if (patchInput.getPrice() != null)
        previousProduct.updatePrice(
            patchInput.getPrice());
      if (patchInput.getStock() != null)
        previousProduct.updateStock(
            patchInput.getStock());
      if (patchInput.getType() != null)
        previousProduct.updateType(
            patchInput.getType());
      if (patchInput.getCategoryCode() != null) {
        Category category = categoryRepository.findByCategoryCode(
            patchInput.getCategoryCode());
        if (category == null) {
          throw new ResponseStatusException(
              ResponseCode.NOT_FOUND.getStatus(),
              ResponseCode.NOT_FOUND.getMessage("카테고리"));
        }
        previousProduct.updateCategory(category);
      }

      if (patchInput.getDescription() != null)
        previousProduct.updateDescription(patchInput.getDescription());

      if (newImgBytes != null) {
        previousProduct.updateImg(newImgBytes);
      }

      if (patchInput.getBarcode() != null)
        previousProduct.updateBarcode(
            patchInput.getBarcode());

      productRepository.save(previousProduct);
    } else {
      throw new ResponseStatusException(
          ResponseCode.UNCHANGED.getStatus(),
          ResponseCode.UNCHANGED.getMessage("제품"));
    }
  }

  public void deleteProduct(String productCode) {
    Product previousProduct = commonUtil.findProductByCode(productCode);
    productRepository.delete(previousProduct);
  }
}
