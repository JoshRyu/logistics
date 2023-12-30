package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.StoreStatisticsRes;
import com.madeg.logistics.service.StoreStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Store Statistics")
@RestController
@RequestMapping("/api/v1/store-statistics")
public class StoreStatisticsController {

  @Autowired
  private StoreStatisticsService storeStatisticsService;

  @Operation(summary = "Create Or Update Store Statistics Monthly")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PostMapping("/{store_code}")
  public ResponseEntity<Object> calculateStatistics(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @RequestParam @NotNull @Min(1900) Integer year,
    @RequestParam @NotNull @Min(1) @Max(12) Integer month
  ) {
    try {
      storeStatisticsService.calculateStatistics(storeCode, year, month);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
          new CommonRes(
            HttpStatus.CREATED.value(),
            "STORE STATISTICS IS CALCULATED"
          )
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get the list of Store Statistics")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = Page.class))
  )
  @GetMapping("/{store_code}")
  public ResponseEntity<StoreStatisticsRes> getStoreStatisticsByStore(
    @PathVariable(name = "store_code", required = true) String storeCode,
    @PageableDefault(size = 10, page = 0) Pageable pageable
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(storeStatisticsService.getStoreStatistics(storeCode, pageable));
  }
}
