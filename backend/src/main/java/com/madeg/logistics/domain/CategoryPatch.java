package com.madeg.logistics.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class CategoryPatch {

    @Pattern(regexp = "^.{1,29}$", message = "카테고리 명은 2자 이상 30자 미만이어야 합니다.")
    private String name;

    private String description;

    private String ParentCategoryName;

}
