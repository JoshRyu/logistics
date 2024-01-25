package com.madeg.logistics.domain;

import com.madeg.logistics.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductPatch {

  @NotBlank
  @Schema(example = "category_1")
  private String categoryCode;

  @NotBlank
  @Pattern(regexp = "^.{1,29}$", message = "제품 명은 1자 이상 30자 미만이어야 합니다.")
  @Schema(example = "곰돌곰돌목도리")
  private String name;

  @Schema(example = "MATERIAL")
  private ProductType type;

  @NotNull
  @Schema(example = "1300")
  @DecimalMin(value = "0.0")
  private BigDecimal price;

  @NotNull
  @Schema(example = "10")
  private Double stock;

  private MultipartFile img;

  private String barcode;

  private String description;
}
