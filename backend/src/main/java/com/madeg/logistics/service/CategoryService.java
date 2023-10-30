package com.madeg.logistics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void createCategory(CategoryInput categoryInput) {
        Category existCategory = categoryRepository.findByName(categoryInput.getCategoryName());

        if (existCategory != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("CATEGORY %s ALREADY EXIST", categoryInput.getCategoryName()));
        }

        Category parentCategory = categoryRepository.findByName(categoryInput.getParentCategoryName());

        if (categoryInput.getParentCategoryName() != null && parentCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PARENT CATEGORY NOT FOUND");
        }

        Category category = Category.builder()
                .description(categoryInput.getDescription())
                .name(categoryInput.getCategoryName())
                .parentCategory(parentCategory)
                .build();

        categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void patchCategory(String code, CategoryPatch patchInput) {
        if (patchInput.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CATEGORY NAME CANNOT BE NULL");
        }

        Category previousCategory = categoryRepository.findByCategoryCode(code);

        if (previousCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CATEGORY NOT FOUND");
        }

        boolean isUpdated = false;

        if (!patchInput.getName().equals(previousCategory.getName())) {
            previousCategory.updateName(patchInput.getName());
            isUpdated = true;
        }

        if (patchInput.getDescription() != null) {
            if (!patchInput.getDescription().equals(previousCategory.getDescription())) {
                previousCategory.updateDescription(patchInput.getDescription());
                isUpdated = true;
            }
        } else {
            if (previousCategory.getDescription() != null) {
                previousCategory.updateDescription(null);
                isUpdated = true;
            }
        }

        if (patchInput.getParentCategoryCode() != null) {
            if (previousCategory.getParentCategory() == null || !patchInput.getParentCategoryCode()
                    .equals(previousCategory.getParentCategory().getCategoryCode())) {
                Category parentCategory = categoryRepository.findByCategoryCode(patchInput.getParentCategoryCode());
                if (parentCategory == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PARENT CATEGORY NOT FOUND");
                }
                previousCategory.updateParentCategory(parentCategory);
                isUpdated = true;
            }
        } else {
            if (previousCategory.getParentCategory() != null) {
                previousCategory.updateParentCategory(null);
                isUpdated = true;
            }
        }

        if (isUpdated) {
            categoryRepository.save(previousCategory);
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "CATEGORY IS NOT UPDATED");
        }
    }

    public void deleteCategory(String code) {
        Category previousCategory = categoryRepository.findByCategoryCode(code);
        if (previousCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "CATEGORY NOT FOUND");
        }
        categoryRepository.delete(previousCategory);
    }
}