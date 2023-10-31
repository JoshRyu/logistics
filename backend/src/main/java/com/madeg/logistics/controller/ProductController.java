package com.madeg.logistics.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.domain.ProductPatch;
import com.madeg.logistics.domain.ResponseCommon;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid ProductInput productInput,
            Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseCommon(HttpStatus.BAD_REQUEST.value(),
                            errors.getFieldError().getDefaultMessage()));
        }

        try {
            productService.createProduct(productInput);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseCommon(HttpStatus.CREATED.value(), "PRODUCT IS CREATED"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
        }
    }

    @GetMapping("/list")
    public List<Product> getProductList() {
        return productService.getProducts();
    }

    @PatchMapping("/{code}")
    public ResponseEntity<Object> patch(@PathVariable(name = "code", required = true) String code,
            @RequestBody @Valid ProductPatch patchInput, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseCommon(HttpStatus.BAD_REQUEST.value(),
                            errors.getFieldError().getDefaultMessage()));
        }
        try {
            productService.patchProduct(code, patchInput);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ResponseCommon(HttpStatus.ACCEPTED.value(), "PRODUCT IS UPDATED"));

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Object> delete(@PathVariable(name = "code", required = true) String code) {
        try {
            productService.deleteProduct(code);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
        }
    }
}
