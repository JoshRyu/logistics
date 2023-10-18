package com.madeg.logistics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void createCategory(CategoryInput categoryInput) {

        Category existCategory = categoryRepository.findByName(categoryInput.getCategoryName());

        if (existCategory != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "409: CATEGORY ALREADY EXIST");
        }

        Category parentCategory = categoryRepository.findByName(categoryInput.getParentCategoryName());

        if (categoryInput.getParentCategoryName() != null && parentCategory == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "404: PARENT CATEGORY NOT FOUND");
        }

        Category category = (Category.builder().description(categoryInput.getDescription())
                .name(categoryInput.getCategoryName())).parentCategory(parentCategory).build();

        categoryRepository.save(category);
    }

}
