package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPatch {

  @NotBlank
  @Pattern(
    regexp = "^.{2,29}$",
    message = "카테고리 명은 2자 이상 30자 미만이어야 합니다."
  )
  @Schema(example = "오렌지")
  private String name;

  private String description;

  @Schema(example = "category_1")
  private String parentCategoryCode;
}
