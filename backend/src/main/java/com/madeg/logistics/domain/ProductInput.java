package com.madeg.logistics.domain;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductInput {

    @NotBlank(message = "제품 명은 반드시 있어야 합니다")
    @Pattern(regexp = "^.{4,29}$", message = "사용자 명은 5자 이상 30자 미만이어야 합니다.")
    private String name;

    @NotNull(message = "카테고리는 반드시 하나 선택해야 합니다")
    private String categoryName;

    @NotNull(message = "가격은 반드시 있어야 합니다")
    private BigDecimal price;

    @NotNull(message = "재고 수량은 반드시 있어야 합니다")
    private int stock;

    private String img;

    private String barcode;

    private String description;

}
