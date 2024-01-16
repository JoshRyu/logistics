package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.SalesHistoryInput;
import com.madeg.logistics.domain.SalesHistoryPatch;
import com.madeg.logistics.domain.SalesHistoryRes;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.service.SalesHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

@Tag(name = "Sales History")
@RestController
@RequestMapping("/api/v1/sales-history")
public class SalesHistoryController {

  @Autowired
  private SalesHistoryService salesHistoryService;

  @Operation(summary = "Register sales history")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PostMapping("/store/{store_code}/product/{product_code}")
  public ResponseEntity<Object> register(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode,
      @RequestBody @Valid SalesHistoryInput salesHistoryInput) {
    try {
      salesHistoryService.registerSalesHistory(
          storeCode,
          productCode,
          salesHistoryInput);
      return ResponseEntity
          .status(ResponseCode.CREATED.getStatus())
          .body(new CommonRes(ResponseCode.CREATED.getCode(), ResponseCode.CREATED.getMessage("매장 판매 이력")));

    } catch (ResponseStatusException ex) {
      return ResponseEntity
          .status(ex.getStatusCode())
          .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get the list of all Sales History By Store and Product")
  @ApiResponse(content = @Content(schema = @Schema(implementation = Page.class)))
  @GetMapping("/store/{store_code}/product/{product_code}")
  public ResponseEntity<SalesHistoryRes> getSalesHistoryByStoreAndProduct(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode,
      @PageableDefault(size = 10, page = 0) Pageable pageable) {

    SalesHistoryRes salesHistory;
    try {
      salesHistory = salesHistoryService.getSalesHistoryByStoreAndProduct(
          storeCode,
          productCode,
          pageable);

    } catch (Exception e) {
      return ResponseEntity.status(ResponseCode.INTERNAL_ERROR.getStatus()).body(
          new SalesHistoryRes(ResponseCode.INTERNAL_ERROR.getCode(), e.getMessage(), null, null));
    }
    return ResponseEntity
        .status(ResponseCode.RETRIEVED.getStatus())
        .body(salesHistory);
  }

  @Operation(summary = "Get the list of all Sales History in the Store in specific month")
  @ApiResponse(content = @Content(schema = @Schema(implementation = Page.class)))
  @GetMapping("/store/{store_code}")
  public ResponseEntity<SalesHistoryRes> getSalesHistoryByStoreAndSalesMonth(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @RequestParam @NotNull @Min(1900) Integer salesYear,
      @RequestParam @NotNull @Min(1) @Max(12) Integer salesMonth,
      @PageableDefault(size = 10, page = 0) Pageable pageable) {

    SalesHistoryRes salesHistory;
    try {
      salesHistory = salesHistoryService.getSalesHistoryByStoreAndSalesMonth(
          storeCode,
          salesYear,
          salesMonth,
          pageable);

    } catch (Exception e) {
      return ResponseEntity.status(ResponseCode.INTERNAL_ERROR.getStatus()).body(
          new SalesHistoryRes(ResponseCode.INTERNAL_ERROR.getCode(), e.getMessage(), null, null));
    }
    return ResponseEntity
        .status(ResponseCode.RETRIEVED.getStatus())
        .body(salesHistory);
  }

  @Operation(summary = "Update Sales History Of Store Product")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PatchMapping("/store/{store_code}/product/{product_code}")
  public ResponseEntity<Object> patch(
      @PathVariable(name = "store_code", required = true) String storeCode,
      @PathVariable(name = "product_code", required = true) String productCode,
      @RequestBody @Valid SalesHistoryPatch patchInput) {
    try {
      salesHistoryService.patchSalesHistory(storeCode, productCode, patchInput);
      return ResponseEntity
          .status(ResponseCode.UPDATED.getStatus())
          .body(new CommonRes(ResponseCode.UPDATED.getCode(), ResponseCode.UPDATED.getMessage("판매 이력")));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
          .status(ex.getStatusCode())
          .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
