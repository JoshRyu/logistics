package com.madeg.logistics.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPatch {

  @NotBlank
  private String categoryCode;

  @NotBlank
  private String name;

  @NotNull
  private BigDecimal price;

  @NotNull
  private int stock;

  private String img;

  private String barcode;

  private String description;
}
