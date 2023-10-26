package com.madeg.logistics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.repository.CategoryRepository;
import com.madeg.logistics.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void createProduct(ProductInput productInput) {

        Product existProduct = productRepository.findByName(productInput.getName());

        if (existProduct != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "PRODUCT ALREADY EXIST");
        }

        Category existCategory = categoryRepository.findByName(productInput.getCategoryName());

        if (existCategory == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CATEGORY NOT FOUND");
        }

        Product product = Product.builder().category(existCategory)
                .name(productInput.getName())
                .price(productInput.getPrice())
                .stock(productInput.getStock())
                .img(productInput.getImg())
                .barcode(productInput.getBarcode())
                .description(productInput.getDescription())
                .build();

        productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(String code) {
        Product previousProduct = productRepository.findByProductCode(code);

        if (previousProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "PRODUCT NOT FOUND");
        }
        productRepository.delete(previousProduct);
    }
}
