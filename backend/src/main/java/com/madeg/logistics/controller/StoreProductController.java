package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.StoreProductInput;
import com.madeg.logistics.domain.StoreProductPatch;
import com.madeg.logistics.domain.StoreProductRes;
import com.madeg.logistics.service.StoreProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Store Product")
@RestController
@RequestMapping("/api/v1/store")
public class StoreProductController {

  @Autowired
  private StoreProductService storeProductService;

  @Operation(summary = "Register a product to the store")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PostMapping("/{store_code}/product")
  public ResponseEntity<Object> register(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @RequestBody @Valid StoreProductInput storeProductInput,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          new CommonRes(
            HttpStatus.BAD_REQUEST.value(),
            errors.getFieldError().getDefaultMessage()
          )
        );
    }

    try {
      storeProductService.registerStoreProduct(storeCode, storeProductInput);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
          new CommonRes(
            HttpStatus.CREATED.value(),
            "PRODUCT IS REGISTERED IN SPECIFIC STORE"
          )
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get the list of all Products in the Store")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = Page.class))
  )
  @GetMapping("/{store_code}/product")
  public ResponseEntity<StoreProductRes> getProductsInStore(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PageableDefault(size = 10, page = 0) Pageable pageable
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(storeProductService.getStoreProducts(storeCode, pageable));
  }

  @Operation(
    summary = "Update a Specific Store Product By Store and Product Code"
  )
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PatchMapping("/{store_code}/product/{product_code}")
  public ResponseEntity<Object> patch(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PathVariable(name = "product_code", required = true) String productCode,
    @RequestBody @Valid StoreProductPatch patchInput,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          new CommonRes(
            HttpStatus.BAD_REQUEST.value(),
            errors.getFieldError().getDefaultMessage()
          )
        );
    }

    try {
      storeProductService.patchStoreProduct(storeCode, productCode, patchInput);
      return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(
          new CommonRes(HttpStatus.ACCEPTED.value(), "STORE PRODUCT IS UPDATED")
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @DeleteMapping("/{store_code}/product/{product_code}")
  public ResponseEntity<Object> delete(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PathVariable(name = "product_code", required = true) String productCode
  ) {
    try {
      storeProductService.deleteStoreProduct(storeCode, productCode);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @PatchMapping("/{store_code}/product/{product_code}/restock")
  public ResponseEntity<Object> restock(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PathVariable(name = "product_code", required = true) String productCode,
    @RequestParam @Valid Integer restockCnt
  ) {
    try {
      storeProductService.restockStoreProduct(
        storeCode,
        productCode,
        restockCnt
      );
      return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(
          new CommonRes(
            HttpStatus.ACCEPTED.value(),
            "STORE PRODUCT IS RESTOCKED"
          )
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
