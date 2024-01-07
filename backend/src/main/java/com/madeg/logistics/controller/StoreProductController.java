package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.StoreProductInput;
import com.madeg.logistics.domain.StoreProductPatch;
import com.madeg.logistics.domain.StoreProductRes;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.service.StoreProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PostMapping("/{store_code}/product/{product_code}")
  public ResponseEntity<Object> register(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode,
      @RequestBody @Valid StoreProductInput storeProductInput) {
    try {
      storeProductService.registerStoreProduct(
          storeCode,
          productCode,
          storeProductInput);
      return ResponseEntity
          .status(ResponseCode.CREATED.getStatus())
          .body(new CommonRes(ResponseCode.CREATED.getCode(), ResponseCode.CREATED.getMessage("매장 제품")));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
          .status(ex.getStatusCode())
          .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get the list of all Products in the Store")
  @ApiResponse(content = @Content(schema = @Schema(implementation = Page.class)))
  @GetMapping("/{store_code}/product")
  public ResponseEntity<StoreProductRes> getProductsInStore(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PageableDefault(size = 10, page = 0) Pageable pageable) {
    return ResponseEntity
        .status(ResponseCode.RETRIEVED.getStatus())
        .body(storeProductService.getStoreProducts(storeCode, pageable));
  }

  @Operation(summary = "Update a Specific Store Product By Store and Product Code")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PatchMapping("/{store_code}/product/{product_code}")
  public ResponseEntity<Object> patch(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode,
      @RequestBody @Valid StoreProductPatch patchInput) {
    try {
      storeProductService.patchStoreProduct(storeCode, productCode, patchInput);
      return ResponseEntity
          .status(ResponseCode.UPDATED.getStatus())
          .body(new CommonRes(ResponseCode.UPDATED.getCode(), ResponseCode.UPDATED.getMessage("매장 제품")));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
          .status(ex.getStatusCode())
          .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Delete a Specific Store Product")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PatchMapping("/{store_code}/product/{product_code}/disable")
  public ResponseEntity<Object> delete(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode) {
    try {
      storeProductService.disableStoreProduct(storeCode, productCode);
      return ResponseEntity
          .status(ResponseCode.SUCCESS.getStatus())
          .body(
              new CommonRes(
                  ResponseCode.SUCCESS.getCode(),
                  ResponseCode.SUCCESS.getMessage("매장 제품 비활성화")));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
          .status(ex.getStatusCode())
          .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Restock Store Product")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PatchMapping("/{store_code}/product/{product_code}/restock")
  public ResponseEntity<Object> restock(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode,
      @RequestParam @Valid Integer restockCnt) {
    try {
      storeProductService.restockStoreProduct(
          storeCode,
          productCode,
          restockCnt);
      return ResponseEntity
          .status(ResponseCode.SUCCESS.getStatus())
          .body(
              new CommonRes(
                  ResponseCode.SUCCESS.getCode(),
                  ResponseCode.SUCCESS.getMessage("매장 제품 재입고")));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
          .status(ex.getStatusCode())
          .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Update Defected Store Product Info")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PatchMapping("/{store_code}/product/{product_code}/defect")
  public ResponseEntity<Object> defect(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode,
      @RequestParam @Valid @Min(0) Integer defectCnt) {
    try {
      storeProductService.updateDefectedStoreProduct(
          storeCode,
          productCode,
          defectCnt);
      return ResponseEntity
          .status(ResponseCode.UPDATED.getStatus())
          .body(
              new CommonRes(
                  ResponseCode.UPDATED.getCode(),
                  ResponseCode.UPDATED.getMessage("매장 제품 결함 정보")));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
          .status(ex.getStatusCode())
          .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
