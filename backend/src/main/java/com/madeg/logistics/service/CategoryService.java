package com.madeg.logistics.service;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.repository.CategoryRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService extends CommonService {

  @Autowired
  private CategoryRepository categoryRepository;

  public void createCategory(CategoryInput categoryInput) {
    Category existCategory = categoryRepository.findByName(
        categoryInput.getName());

    if (existCategory != null) {
      throw new ResponseStatusException(
          ResponseCode.CONFLICT.getStatus(),
          ResponseCode.CONFLICT.getMessage("카테고리"));
    }

    Category parentCategory = categoryRepository.findByCategoryCode(
        categoryInput.getParentCategoryCode());

    if (categoryInput.getParentCategoryCode() != null && parentCategory == null) {
      throw new ResponseStatusException(
          ResponseCode.NOT_FOUND.getStatus(),
          ResponseCode.NOT_FOUND.getMessage("부모 카테고리"));
    }

    Category category = Category
        .builder()
        .description(categoryInput.getDescription())
        .name(categoryInput.getName())
        .parentCategory(parentCategory)
        .build();

    categoryRepository.save(category);
  }

  public List<Category> getCategories() {
    return categoryRepository.findAll();
  }

  public void patchCategory(String categoryCode, CategoryPatch patchInput) {
    Category previousCategory = findCategoryByCode(categoryCode);

    if (categoryRepository.findByCategoryCode(patchInput.getName()) != null)
      throw new ResponseStatusException(
          ResponseCode.CONFLICT.getStatus(),
          ResponseCode.CONFLICT.getMessage("카테고리 이름"));

    if (patchInput.getParentCategoryCode() != null &&
        patchInput.getParentCategoryCode().equals(categoryCode)) {
      throw new ResponseStatusException(
          ResponseCode.BAD_REQUEST.getStatus(),
          ResponseCode.BAD_REQUEST.getMessage("부모 카테고리 코드는 카테고리 코드와 달라야 합니다"));
    }

    Category parentCategory = null;
    if (patchInput.getParentCategoryCode() != null) {
      parentCategory = categoryRepository.findByCategoryCode(
          patchInput.getParentCategoryCode());
      if (parentCategory == null) {
        throw new ResponseStatusException(
            ResponseCode.NOT_FOUND.getStatus(),
            ResponseCode.NOT_FOUND.getMessage("부모 카테고리"));
      }
    }
    Category updatedCategory = new Category();
    updatedCategory.updateName(patchInput.getName());
    updatedCategory.updateDescription(patchInput.getDescription());
    updatedCategory.updateParentCategory(parentCategory);

    if (previousCategory.isStateChanged(updatedCategory)) {
      previousCategory.updateName(updatedCategory.getName());
      previousCategory.updateDescription(updatedCategory.getDescription());
      previousCategory.updateParentCategory(
          updatedCategory.getParentCategory());
      categoryRepository.save(previousCategory);
    } else {
      throw new ResponseStatusException(
          ResponseCode.UNCHANGED.getStatus(),
          ResponseCode.UNCHANGED.getMessage("카테고리"));
    }
  }

  public void deleteCategory(String categoryCode) {
    Category previousCategory = findCategoryByCode(categoryCode);
    categoryRepository.delete(previousCategory);
  }
}
