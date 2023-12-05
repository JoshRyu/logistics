package com.madeg.logistics.service;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.repository.CategoryRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public void createCategory(CategoryInput categoryInput) {
    Category existCategory = categoryRepository.findByName(
      categoryInput.getCategoryName()
    );

    if (existCategory != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        String.format(
          "CATEGORY %s ALREADY EXIST",
          categoryInput.getCategoryName()
        )
      );
    }

    Category parentCategory = categoryRepository.findByCategoryCode(
      categoryInput.getParentCategoryCode()
    );

    if (
      categoryInput.getParentCategoryCode() != null && parentCategory == null
    ) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "PARENT CATEGORY NOT FOUND"
      );
    }

    Category category = Category
      .builder()
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
    Category previousCategory = categoryRepository.findByCategoryCode(code);

    if (previousCategory == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "CATEGORY NOT FOUND"
      );
    }

    boolean isUpdated = false;

    if (!Objects.equals(patchInput.getName(), previousCategory.getName())) {
      previousCategory.updateName(patchInput.getName());
      isUpdated = true;
    }

    if (
      !Objects.equals(
        patchInput.getDescription(),
        previousCategory.getDescription()
      )
    ) {
      previousCategory.updateDescription(patchInput.getDescription());
      isUpdated = true;
    }

    String parentCategoryCode = patchInput.getParentCategoryCode();
    Category currentParentCategory = previousCategory.getParentCategory();

    if (
      !Objects.equals(
        parentCategoryCode,
        currentParentCategory != null
          ? currentParentCategory.getCategoryCode()
          : null
      )
    ) {
      Category parentCategory = categoryRepository.findByCategoryCode(
        patchInput.getParentCategoryCode()
      );
      if (parentCategory == null) {
        throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "PARENT CATEGORY NOT FOUND"
        );
      }
      previousCategory.updateParentCategory(parentCategory);
      isUpdated = true;
    } else if (parentCategoryCode == null && currentParentCategory != null) {
      previousCategory.updateParentCategory(null);
      isUpdated = true;
    }

    if (isUpdated) {
      categoryRepository.save(previousCategory);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "CATEGORY IS NOT UPDATED"
      );
    }
  }

  public void deleteCategory(String code) {
    Category previousCategory = categoryRepository.findByCategoryCode(code);
    if (previousCategory == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "CATEGORY NOT FOUND"
      );
    }
    categoryRepository.delete(previousCategory);
  }
}
