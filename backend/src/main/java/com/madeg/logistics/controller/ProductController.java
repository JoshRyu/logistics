package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.ProductInput;
import com.madeg.logistics.domain.ProductPatch;
import com.madeg.logistics.domain.ProductRes;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.enums.ProductType;
import com.madeg.logistics.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Product")
@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @Operation(summary = "Register new Product")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PostMapping(consumes = { "multipart/form-data" })
  public ResponseEntity<Object> create(
    @ModelAttribute @Valid ProductInput productInput
  ) {
    try {
      productService.createProduct(productInput);

      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new CommonRes(HttpStatus.CREATED.value(), "PRODUCT IS CREATED"));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get All Product List")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = Page.class))
  )
  @GetMapping
  public ResponseEntity<ProductRes> getProductList(
    @RequestParam(required = false) ProductType type,
    @PageableDefault(
      size = 10,
      page = 0,
      sort = "productCode",
      direction = Sort.Direction.ASC
    ) Pageable pageable
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(productService.getProducts(type, pageable));
  }

  @Operation(summary = "Get a Specific Product by Code")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = List.class))
  )
  @GetMapping("/{product_code}")
  public ResponseEntity<ProductRes> getProductByCode(
    @PathVariable(name = "product_code", required = true) String productCode
  ) {
    try {
      Product product = productService.getProductByCode(productCode);

      return ResponseEntity
        .status(HttpStatus.OK)
        .body(
          new ProductRes(
            HttpStatus.OK.value(),
            "PRODUCT is RETRIEVED",
            Collections.singletonList(product),
            null
          )
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(
          new ProductRes(
            ex.getStatusCode().value(),
            ex.getReason(),
            new ArrayList<>(),
            null
          )
        );
    }
  }

  @Operation(summary = "Update a Specific Product by Code")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PatchMapping(value = "/{product_code}", consumes = { "multipart/form-data" })
  public ResponseEntity<Object> patch(
    @PathVariable(name = "product_code", required = true) String productCode,
    @ModelAttribute @Valid ProductPatch patchInput
  ) {
    try {
      productService.patchProduct(productCode, patchInput);
      return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(new CommonRes(HttpStatus.ACCEPTED.value(), "PRODUCT IS UPDATED"));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Delete a Specific Product by Code")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @DeleteMapping("/{product_code}")
  public ResponseEntity<Object> delete(
    @PathVariable(name = "product_code", required = true) String productCode
  ) {
    try {
      productService.deleteProduct(productCode);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
