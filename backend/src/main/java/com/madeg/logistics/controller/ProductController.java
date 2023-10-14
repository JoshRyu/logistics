package com.madeg.logistics.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.madeg.logistics.util.ErrorUtil;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ErrorUtil errorUtil;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid ProductInput productInput,
            Errors errors) {

        if (errors.hasErrors()) {
            return new ResponseEntity<>(errorUtil.getErrorMessages(errors), HttpStatus.BAD_REQUEST);
        }

        productService.createProduct(productInput);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
