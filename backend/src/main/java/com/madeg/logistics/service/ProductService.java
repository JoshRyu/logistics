package com.madeg.logistics.service;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.domain.ProductPatch;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.repository.CategoryRepository;
import com.madeg.logistics.repository.ProductRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

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

    Category existCategory = categoryRepository.findByName(
      productInput.getCategoryName()
    );

    if (existCategory == null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "CATEGORY NOT FOUND"
      );
    }

    byte[] imageBytes;
    try {
      imageBytes = productInput.getImg().getBytes();
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
      .img(imageBytes)
      .barcode(productInput.getBarcode())
      .description(productInput.getDescription())
      .build();

    productRepository.save(product);
  }

  public List<Product> getProducts() {
    return productRepository.findAll();
  }

  public Product getProductByCode(String code) {
    return productRepository.findByProductCode(code);
  }

  public void patchProduct(String code, ProductPatch patchInput) {
    Product previousProduct = productRepository.findByProductCode(code);

    if (previousProduct == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "PRODUCT NOT FOUND"
      );
    }

    Boolean isUpdated = false;

    if (!patchInput.getName().equals(previousProduct.getName())) {
      previousProduct.updateName(patchInput.getName());
      isUpdated = true;
    }

    if (patchInput.getPrice().compareTo(previousProduct.getPrice()) != 0) {
      previousProduct.updatePrice(patchInput.getPrice());
      isUpdated = true;
    }

    if (patchInput.getStock() != previousProduct.getStock()) {
      previousProduct.updateStock(patchInput.getStock());
      isUpdated = true;
    }

    if (
      previousProduct.getCategory() == null ||
      !patchInput
        .getCategoryCode()
        .equals(previousProduct.getCategory().getCategoryCode())
    ) {
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
      isUpdated = true;
    }

    if (patchInput.getDescription() != null) {
      if (
        !patchInput.getDescription().equals(previousProduct.getDescription())
      ) {
        previousProduct.updateDescription(patchInput.getDescription());
        isUpdated = true;
      }
    } else {
      if (previousProduct.getDescription() != null) {
        previousProduct.updateDescription(null);
        isUpdated = true;
      }
    }

    if (patchInput.getImg() != null) {
      try {
        byte[] newImg = patchInput.getImg().getBytes();
        if (!Arrays.equals(newImg, previousProduct.getImg())) {
          previousProduct.updateImg(newImg);
          isUpdated = true;
        }
      } catch (IOException e) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "IMAGE INPUT IS INVALID"
        );
      }
    } else {
      if (previousProduct.getImg() != null) {
        previousProduct.updateImg(null);
        isUpdated = true;
      }
    }

    if (patchInput.getBarcode() != null) {
      if (!patchInput.getBarcode().equals(previousProduct.getBarcode())) {
        previousProduct.updateBarcode(patchInput.getBarcode());
        isUpdated = true;
      }
    } else {
      if (previousProduct.getBarcode() != null) {
        previousProduct.updateBarcode(null);
        isUpdated = true;
      }
    }

    if (isUpdated) {
      productRepository.save(previousProduct);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "PRODUCT IS NOT UPDATED"
      );
    }
  }

  public void deleteProduct(String code) {
    Product previousProduct = productRepository.findByProductCode(code);

    if (previousProduct == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "PRODUCT NOT FOUND"
      );
    }
    productRepository.delete(previousProduct);
  }
}
