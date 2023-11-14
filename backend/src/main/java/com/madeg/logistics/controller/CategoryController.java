package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.domain.CategoryRes;
import com.madeg.logistics.domain.ResponseCommon;
import com.madeg.logistics.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Category")
@RestController
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @Operation(summary = "Register new Category")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = ResponseCommon.class))
  )
  @PostMapping
  public ResponseEntity<Object> create(
    @RequestBody @Valid CategoryInput categoryInput,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          new ResponseCommon(
            HttpStatus.BAD_REQUEST.value(),
            errors.getFieldError().getDefaultMessage()
          )
        );
    }

    try {
      categoryService.createCategory(categoryInput);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
          new ResponseCommon(HttpStatus.CREATED.value(), "CATEGORY IS CREATED")
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get All Category List")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = List.class))
  )
  @GetMapping
  public ResponseEntity<CategoryRes> getCategoryList() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        new CategoryRes(
          HttpStatus.OK.value(),
          "CATEGORIES ARE RETRIEVED",
          categoryService.getCategories()
        )
      );
  }

  @Operation(summary = "Update a Specific Category by Code")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = ResponseCommon.class))
  )
  @PatchMapping("/{code}")
  public ResponseEntity<Object> patch(
    @PathVariable(name = "code", required = true) String code,
    @RequestBody @Valid CategoryPatch patchInput,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          new ResponseCommon(
            HttpStatus.BAD_REQUEST.value(),
            errors.getFieldError().getDefaultMessage()
          )
        );
    }
    try {
      categoryService.patchCategory(code, patchInput);
      return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(
          new ResponseCommon(HttpStatus.ACCEPTED.value(), "CATEGORY IS UPDATED")
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Delete a Specific Category by Code")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = ResponseCommon.class))
  )
  @DeleteMapping("/{code}")
  public ResponseEntity<Object> delete(
    @PathVariable(name = "code", required = true) String code
  ) {
    try {
      categoryService.deleteCategory(code);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
