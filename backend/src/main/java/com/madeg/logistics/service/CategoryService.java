package com.madeg.logistics.service;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.repository.CategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService extends CommonService {

  @Autowired
  private CategoryRepository categoryRepository;

  public void createCategory(CategoryInput categoryInput) {
    Category existCategory = categoryRepository.findByName(
      categoryInput.getName()
    );

    if (existCategory != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        String.format("CATEGORY %s ALREADY EXIST", categoryInput.getName())
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

    if (categoryRepository.findByCategoryCode(patchInput.getName()) != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "CATEGORY NAME ALREADY EXIST"
      );
    }

    String parentCategoryCode = patchInput.getParentCategoryCode();
    if (parentCategoryCode.equals(categoryCode)) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "PARENT CATEGORY CODE SHOULD NOT BE EQUELS TO CATEGORY CODE"
      );
    }

    Category updatedCategory = new Category();
    updatedCategory.updateName(patchInput.getName());
    updatedCategory.updateDescription(patchInput.getDescription());

    Category parentCategory = categoryRepository.findByCategoryCode(
      parentCategoryCode
    );
    if (parentCategory == null) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "PARENT CATEGORY NOT FOUND"
      );
    }
    updatedCategory.updateParentCategory(parentCategory);

    if (previousCategory.isStateChanged(updatedCategory)) {
      previousCategory.updateName(updatedCategory.getName());
      previousCategory.updateDescription(updatedCategory.getDescription());
      previousCategory.updateParentCategory(
        updatedCategory.getParentCategory()
      );

      categoryRepository.save(previousCategory);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NO_CONTENT,
        "CATEGORY IS NOT UPDATED"
      );
    }
  }

  public void deleteCategory(String categoryCode) {
    Category previousCategory = findCategoryByCode(categoryCode);
    categoryRepository.delete(previousCategory);
  }
}
