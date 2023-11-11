package com.madeg.logistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "custom_sequence"
  )
  @GenericGenerator(
    name = "custom_sequence",
    strategy = "com.madeg.logistics.entity.CustomSequenceGenerator",
    parameters = { @Parameter(name = "prefix", value = "product_code_") }
  )
  @Column(name = "product_code", unique = true, nullable = false)
  private String productCode;

  @ManyToOne
  @JoinColumn(
    name = "category_code",
    referencedColumnName = "category_code",
    nullable = false
  )
  private Category category;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Column(name = "stock", nullable = false)
  private int stock;

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
}
