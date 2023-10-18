package com.madeg.logistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.service.CategoryService;
import com.madeg.logistics.util.ErrorUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ErrorUtil errorUtil;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid CategoryInput categoryInput, Errors errors) {

        if (errors.hasErrors()) {
            return new ResponseEntity<>(errorUtil.getErrorMessages(errors), HttpStatus.BAD_REQUEST);
        }

        categoryService.createCategory(categoryInput);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
