package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.StoreInput;
import com.madeg.logistics.domain.StoreRes;
import com.madeg.logistics.service.StoreService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Store")
@RestController
@RequestMapping(path = "/api/v1/store")
public class StoreController {

  @Autowired
  private StoreService storeService;

  @Operation(summary = "Register new Store")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PostMapping
  public ResponseEntity<Object> create(
    @RequestBody @Valid StoreInput storeInput,
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
      storeService.createStore(storeInput);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new CommonRes(HttpStatus.CREATED.value(), "STORE IS CREATED"));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get All Store List")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = List.class))
  )
  @GetMapping
  public ResponseEntity<StoreRes> getStoreList() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        new StoreRes(
          HttpStatus.OK.value(),
          "STORES ARE RETRIEVED",
          storeService.getStores()
        )
      );
  }
}
