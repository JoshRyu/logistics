package com.madeg.logistics.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.domain.ResponseCommon;
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

}
