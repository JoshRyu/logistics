package com.madeg.logistics.domain;

import com.madeg.logistics.enums.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreInput {

  @NotBlank(message = "상점 명은 반드시 있어야 합니다")
  @Pattern(
    regexp = "^.{2,29}$",
    message = "상점 명은 2자 이상 30자 미만이어야 합니다."
  )
  @Schema(example = "아이디어스")
  private String name;

  @Schema(example = "경기도 용인시 수지구 88번지")
  private String address;

  @NotNull(message = " 상점 유형은 반드시 있어야 합니다")
  @Schema(example = "FIX")
  private StoreType type;

  @Schema(example = "30000")
  private Integer fixedCost;

  @Schema(example = "3.5")
  private Double commissionRate;

  @Schema(example = "메인 상점")
  private String description;
}
