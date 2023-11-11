package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class ProductInput {

  @NotBlank(message = "제품 명은 반드시 있어야 합니다")
  @Pattern(
    regexp = "^.{5,29}$",
    message = "제품 명은 5자 이상 30자 미만이어야 합니다."
  )
  @Schema(example = "아오리사과")
  private String name;

  @NotNull(message = "카테고리는 반드시 하나 선택해야 합니다")
  @Schema(example = "과일")
  private String categoryName;

  @NotNull(message = "가격은 반드시 있어야 합니다")
  @Schema(example = "1200")
  private BigDecimal price;

  @NotNull(message = "재고 수량은 반드시 있어야 합니다")
  @Schema(example = "11")
  private int stock;

  private MultipartFile file;

  private String barcode;

  private String description;
}
