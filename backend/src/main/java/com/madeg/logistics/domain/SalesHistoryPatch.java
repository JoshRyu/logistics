package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesHistoryPatch {

  @NotNull
  @Schema(example = "2023")
  @Min(1900)
  private Integer salesYear;

  @NotNull
  @Schema(example = "12")
  @Min(1)
  @Max(12)
  private Integer salesMonth;

  @Schema(example = "1")
  @DecimalMin(value = "0")
  private Integer quantity;

  private String memo;
}
