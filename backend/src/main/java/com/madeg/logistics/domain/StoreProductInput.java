package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreProductInput {

  @NotBlank(message = "상점 명은 반드시 있어야 합니다")
  @Schema(example = "store_1")
  private String storeCode;

  @NotBlank(message = "제품 명은 반드시 있어야 합니다")
  @Schema(example = "product_1")
  private String productCode;

  @DecimalMin(value = "0.0")
  @Schema(example = "1000")
  private BigDecimal storePrice;

  @DecimalMin(value = "0")
  @Schema(example = "7")
  private Integer incomeCnt;

  @Schema(example = "메모입니다")
  private String description;
}
