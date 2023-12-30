package com.madeg.logistics.domain;

import com.madeg.logistics.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
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
  @Schema(example = "양털곰돌목도리")
  private String name;

  @Schema(example = "MATERIAL")
  private ProductType type;

  @NotNull(message = "카테고리는 반드시 하나 선택해야 합니다")
  @Schema(example = "category_17")
  private String categoryCode;

  @NotNull(message = "가격은 반드시 있어야 합니다")
  @DecimalMin(value = "0.0")
  @Schema(example = "1200")
  private BigDecimal price;

  @NotNull(message = "재고 수량은 반드시 있어야 합니다")
  @Schema(example = "11")
  private int stock;

  private MultipartFile img;

  private String barcode;

  private String description;
}
