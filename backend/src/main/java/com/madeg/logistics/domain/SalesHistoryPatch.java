package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesHistoryPatch {

  @Schema(example = "1")
  @DecimalMin(value = "0")
  private Integer quantity;

  private String memo;
}
