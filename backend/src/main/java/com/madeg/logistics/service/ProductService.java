package com.madeg.logistics.service;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.domain.ProductPatch;
import com.madeg.logistics.domain.ProductRes;
import com.madeg.logistics.domain.SimplePageInfo;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.enums.ProductType;
import com.madeg.logistics.repository.CategoryRepository;
import com.madeg.logistics.repository.ProductRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService extends CommonService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public void createProduct(ProductInput productInput) {
    Product existProduct = productRepository.findByName(productInput.getName());

    if (existProduct != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "PRODUCT ALREADY EXIST"
      );
    }

    Category existCategory = categoryRepository.findByCategoryCode(
      productInput.getCategoryCode()
    );

    if (existCategory == null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "CATEGORY NOT FOUND"
      );
    }

    byte[] imageBytes = null;

    try {
      if (productInput.getImg() != null) {
        imageBytes = productInput.getImg().getBytes();
      }
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "INVALID IMAGE INPUT"
      );
    }

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

  public ProductRes getProducts(ProductType type, Pageable pageable) {
    Page<Product> page = type == null
      ? productRepository.findAll(pageable)
      : productRepository.findByType(type, pageable);
    List<Product> content = page.getContent();

    SimplePageInfo simplePageInfo = createSimplePageInfo(page);

    return new ProductRes(
      HttpStatus.OK.value(),
      "PRODUCTS ARE RETRIEVED",
      content,
      simplePageInfo
    );
  }

  public Product getProductByCode(String productCode) {
    return findProductByCode(productCode);
  }

  public void patchProduct(String productCode, ProductPatch patchInput) {
    Product previousProduct = findProductByCode(productCode);

    byte[] newImgBytes = null;
    if (patchInput.getImg() != null) {
      try {
        newImgBytes = patchInput.getImg().getBytes();
      } catch (IOException e) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "IMAGE INPUT IS INVALID"
        );
      }
    }

    if (previousProduct.isStateChanged(patchInput, newImgBytes)) {
      if (patchInput.getName() != null) previousProduct.updateName(
        patchInput.getName()
      );
      if (patchInput.getPrice() != null) previousProduct.updatePrice(
        patchInput.getPrice()
      );
      if (patchInput.getStock() != null) previousProduct.updateStock(
        patchInput.getStock()
      );

      if (patchInput.getType() != null) previousProduct.updateType(
        patchInput.getType()
      );

      if (patchInput.getCategoryCode() != null) {
        Category category = categoryRepository.findByCategoryCode(
          patchInput.getCategoryCode()
        );
        if (category == null) {
          throw new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "CATEGORY NOT FOUND"
          );
        }
        previousProduct.updateCategory(category);
      }

      if (
        patchInput.getDescription() != null
      ) previousProduct.updateDescription(patchInput.getDescription());

      if (newImgBytes != null) {
        previousProduct.updateImg(newImgBytes);
      }

      if (patchInput.getBarcode() != null) previousProduct.updateBarcode(
        patchInput.getBarcode()
      );

      productRepository.save(previousProduct);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "PRODUCT IS NOT UPDATED"
      );
    }
  }

  public void deleteProduct(String productCode) {
    Product previousProduct = findProductByCode(productCode);
    productRepository.delete(previousProduct);
  }
}
