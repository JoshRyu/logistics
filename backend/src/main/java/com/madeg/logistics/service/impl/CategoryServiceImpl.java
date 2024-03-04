package com.madeg.logistics.service.impl;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.entity.Category;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.repository.CategoryRepository;
import com.madeg.logistics.service.CategoryService;
import com.madeg.logistics.util.CommonUtil;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CommonUtil commonUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CommonUtil commonUtil) {
        this.categoryRepository = categoryRepository;
        this.commonUtil = commonUtil;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    public CommonRes createCategory(CategoryInput categoryInput) {

        if (categoryRepository.findByName(
                categoryInput.getName()) != null) {
            return new CommonRes(
                    ResponseCode.CONFLICT.getCode(),
                    ResponseCode.CONFLICT.getMessage("카테고리"));
        }

        Category parentCategory = categoryRepository.findByCategoryCode(
                categoryInput.getParentCategoryCode());

        if (categoryInput.getParentCategoryCode() != null && parentCategory == null) {
            return new CommonRes(
                    ResponseCode.NOT_FOUND.getCode(),
                    ResponseCode.NOT_FOUND.getMessage("부모 카테고리"));
        }

        Category category = Category
                .builder()
                .description(categoryInput.getDescription())
                .name(categoryInput.getName())
                .parentCategory(parentCategory)
                .build();

        categoryRepository.save(category);

        return new CommonRes(ResponseCode.CREATED.getCode(), ResponseCode.CREATED.getMessage("카테고리"));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void patchCategory(String categoryCode, CategoryPatch patchInput) {
        Category previousCategory = commonUtil.findCategoryByCode(categoryCode);

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

    @Override
    @Transactional
    public void deleteCategory(String categoryCode) {
        Category previousCategory = commonUtil.findCategoryByCode(categoryCode);
        categoryRepository.delete(previousCategory);
    }
}
