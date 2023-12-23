package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreProductPatch {

  @Schema(example = "1300")
  @DecimalMin(value = "0.0")
  private BigDecimal storePrice;

  private String description;
}
