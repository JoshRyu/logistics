package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.SalesHistoryInput;
import com.madeg.logistics.domain.SalesHistoryPatch;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PostMapping("/store/{store_code}/product/{product_code}")
  public ResponseEntity<Object> register(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PathVariable(name = "product_code", required = true) String productCode,
    @RequestBody @Valid SalesHistoryInput salesHistoryInput,
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
      salesHistoryService.registerSalesHistory(
        storeCode,
        productCode,
        salesHistoryInput
      );
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
          new CommonRes(
            HttpStatus.CREATED.value(),
            "SALES HISTORY IS REGISTERED IN SPECIFIC STORE"
          )
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Update Sales History Of Store Product")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PatchMapping("/store/{store_code}/product/{product_code}")
  public ResponseEntity<Object> patch(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PathVariable(name = "product_code", required = true) String productCode,
    @RequestBody @Valid SalesHistoryPatch patchInput,
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
      salesHistoryService.patchSalesHistory(storeCode, productCode, patchInput);
      return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(
          new CommonRes(HttpStatus.ACCEPTED.value(), "SALES HISTORY IS UPDATED")
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Delete a Specific Sales History")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @DeleteMapping("/store/{store_code}/product/{product_code}")
  public ResponseEntity<Object> delete(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PathVariable(name = "product_code", required = true) String productCode,
    @RequestParam @NotNull @Min(1900) Integer salesYear,
    @RequestParam @NotNull @Min(1) @Max(12) Integer salesMonth
  ) {
    try {
      salesHistoryService.deleteSalesHistory(
        storeCode,
        productCode,
        salesYear,
        salesMonth
      );
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
