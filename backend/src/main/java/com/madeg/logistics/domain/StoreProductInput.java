package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreProductInput {

  @DecimalMin(value = "0.0")
  @Schema(example = "1000")
  private BigDecimal storePrice;

  @DecimalMin(value = "0")
  @Schema(example = "7")
  private Integer incomeCnt;

  @Schema(example = "메모입니다")
  private String description;
}
