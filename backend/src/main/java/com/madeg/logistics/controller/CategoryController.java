package com.madeg.logistics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.ResponseCommon;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid CategoryInput categoryInput, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseCommon(HttpStatus.BAD_REQUEST.value(),
                            errors.getFieldError().getDefaultMessage()));
        }

        try {
            categoryService.createCategory(categoryInput);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseCommon(HttpStatus.CREATED.value(), "CATEGORY IS CREATED"));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
        }
    }

    @GetMapping("/list")
    public List<Category> getCategoryList() {
        return categoryService.getCategories();
    }
}