package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.domain.CategoryRes;
import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category")
@RestController
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Operation(summary = "Register new Category")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PostMapping
  public ResponseEntity<CommonRes> create(
      @RequestBody @Valid CategoryInput categoryInput) {
    try {
      CommonRes commonRes = categoryService.createCategory(categoryInput);
      return ResponseEntity
          .status(commonRes.getStatus())
          .body(commonRes);
    } catch (Exception e) {
      return ResponseEntity
          .status(ResponseCode.INTERNAL_ERROR.getStatus())
          .body(new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage()));
    }
  }

  @Operation(summary = "Get All Category List")
  @ApiResponse(content = @Content(schema = @Schema(implementation = List.class)))
  @GetMapping
  public ResponseEntity<CategoryRes> getCategoryList() {
    return ResponseEntity
        .status(ResponseCode.RETRIEVED.getStatus())
        .body(
            new CategoryRes(
                ResponseCode.RETRIEVED.getCode(),
                ResponseCode.RETRIEVED.getMessage("카테고리 목록"),
                categoryService.getCategories()));
  }

  @Operation(summary = "Update a Specific Category by Code")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PatchMapping("/{category_code}")
  public ResponseEntity<CommonRes> patch(
      @PathVariable(name = "category_code", required = true) String categoryCode,
      @RequestBody @Valid CategoryPatch patchInput) {
    try {
      CommonRes commonRes = categoryService.patchCategory(categoryCode, patchInput);
      return ResponseEntity
          .status(commonRes.getStatus())
          .body(commonRes);
    } catch (Exception e) {
      return ResponseEntity
          .status(ResponseCode.INTERNAL_ERROR.getStatus())
          .body(new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage()));
    }
  }

  @Operation(summary = "Delete a Specific Category by Code")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @DeleteMapping("/{category_code}")
  public ResponseEntity<CommonRes> delete(
      @PathVariable(name = "category_code", required = true) String categoryCode) {
    try {
      CommonRes commonRes = categoryService.deleteCategory(categoryCode);
      return ResponseEntity.status(commonRes.getStatus()).body(commonRes);
    } catch (Exception e) {
      return ResponseEntity
          .status(ResponseCode.INTERNAL_ERROR.getStatus())
          .body(new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage()));
    }
  }
}
