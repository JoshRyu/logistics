package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SalesHistoryInput {

  @NotNull
  @Schema(example = "2023")
  @Min(1900)
  private Integer salesYear;

  @NotNull
  @Schema(example = "12")
  @Min(1)
  @Max(12)
  private Integer salesMonth;

  @DecimalMin(value = "0")
  @Schema(example = "2")
  private Integer quantity;

  @Schema(example = "메모입니다")
  private String memo;
}
