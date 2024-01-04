package com.madeg.logistics.entity;

import com.madeg.logistics.domain.ProductPatch;
import com.madeg.logistics.enums.ProductType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_code_seq")
  @GenericGenerator(name = "product_code_seq", strategy = "com.madeg.logistics.entity.CustomSequenceGenerator", parameters = {
      @Parameter(name = "prefix", value = "product_code_"),
      @Parameter(name = "increment_size", value = "1") })
  @Column(name = "product_code", unique = true, nullable = false)
  private String productCode;

  @ManyToOne
  @JoinColumn(name = "category_code", referencedColumnName = "category_code", nullable = false)
  private Category category;

  @Column(name = "type", nullable = false)
  private ProductType type;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Column(name = "stock", nullable = false)
  private Integer stock;

  @Column(name = "img")
  private byte[] img;

  @Column(name = "barcode")
  private String barcode;

  @Column(name = "description")
  private String description;

  public void updateName(String name) {
    this.name = name;
  }

  public void updatePrice(BigDecimal price) {
    this.price = price;
  }

  public void updateStock(int stock) {
    this.stock = stock;
  }

  public void updateCategory(Category category) {
    this.category = category;
  }

  public void updateImg(byte[] img) {
    this.img = img;
  }

  public void updateBarcode(String barcode) {
    this.barcode = barcode;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public void updateType(ProductType type) {
    this.type = type;
  }

  public boolean isStateChanged(ProductPatch patchInput, byte[] newImgBytes) {
    return (!Objects.equals(name, patchInput.getName()) ||
        (price != null &&
            patchInput.getPrice() != null &&
            price.compareTo(patchInput.getPrice()) != 0)
        ||
        stock != patchInput.getStock() ||
        (category == null
            ? patchInput.getCategoryCode() != null
            : !category.getCategoryCode().equals(patchInput.getCategoryCode()))
        ||
        !Objects.equals(description, patchInput.getDescription()) ||
        !Objects.equals(type, patchInput.getType()) ||
        !Arrays.equals(img, newImgBytes) ||
        !Objects.equals(barcode, patchInput.getBarcode()));
  }
}
