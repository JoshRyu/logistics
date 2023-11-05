package com.madeg.logistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "discount")
public class Discount {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "discount_id_seq"
  )
  @SequenceGenerator(
    name = "discount_id_seq",
    sequenceName = "discount_id_seq",
    allocationSize = 1
  )
  @Column(name = "discount_id", unique = true, nullable = false)
  private Long discountId;

  @ManyToOne
  @JoinColumn(
    name = "store_product_id",
    referencedColumnName = "store_product_id",
    nullable = false
  )
  private StoreProduct storeProduct;

  @Column(name = "discount_month")
  private Integer discountMonth;

  @Column(name = "discount_price")
  private BigDecimal discountPrice;
}
