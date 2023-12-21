package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.StoreProductInput;
import com.madeg.logistics.service.StoreProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Store Product")
@RestController
@RequestMapping("/api/v1/store")
public class StoreProductController {

  @Autowired
  private StoreProductService storeProductService;

  @PostMapping("/{store_code}/product")
  public ResponseEntity<Object> register(
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
      storeProductService.registerStoreProduct(storeProductInput);
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
}
