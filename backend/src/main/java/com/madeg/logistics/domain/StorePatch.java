package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorePatch {

  @NotBlank
  @Pattern(regexp = "^.{2,29}$", message = "상점 명은 2자 이상 30자 미만이어야 합니다.")
  @Schema(example = "네이버 스토어")
  private String name;

  @Schema(example = "경기도 용인시 수지구 88번지")
  private String address;

  @DecimalMin(value = "0", message = "고정 비용은 0 미만이 될 수 없습니다.")
  @Schema(example = "30000")
  private Integer fixedCost;

  @DecimalMin(value = "0.0", message = "수수료율은 0.0 미만이 될 수 없습니다.")
  @DecimalMax(value = "100.0", message = "수수료율은 100.0을 초과할 수 없습니다.")
  @Schema(example = "3.5")
  private Double commissionRate;

  @Schema(example = "메인 상점")
  private String description;
}
