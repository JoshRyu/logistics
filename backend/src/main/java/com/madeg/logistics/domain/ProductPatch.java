package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPatch {

  @NotBlank
  @Schema(example = "category_1")
  private String categoryCode;

  @NotBlank
  @Schema(example = "신선한오렌지")
  private String name;

  @NotNull
  @Schema(example = "1300")
  private BigDecimal price;

  @NotNull
  @Schema(example = "10")
  private int stock;

  private String img;

  private String barcode;

  private String description;
}
