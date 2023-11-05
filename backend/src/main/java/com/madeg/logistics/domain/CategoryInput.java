package com.madeg.logistics.domain;

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
  @Pattern(
    regexp = "^.{2,29}$",
    message = "카테고리 명은 2자 이상 30자 미만이어야 합니다."
  )
  private String categoryName;

  private String ParentCategoryName;

  private String description;
}
