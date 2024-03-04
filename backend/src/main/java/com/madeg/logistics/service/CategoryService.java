package com.madeg.logistics.service;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.entity.Category;
import java.util.List;

public interface CategoryService {

  CommonRes createCategory(CategoryInput categoryInput);

  List<Category> getCategories();

  void patchCategory(String categoryCode, CategoryPatch patchInput);

  void deleteCategory(String categoryCode);
}