package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryInput {

  @NotBlank(message = "카테고리 명은 반드시 있어야 합니다")
  @Pattern(regexp = "^.{1,29}$", message = "카테고리 명은 1자 이상 30자 미만이어야 합니다.")
  @Schema(example = "목도리")
  private String name;

  @Schema(example = "category_15")
  private String parentCategoryCode;

  private String description;
}
