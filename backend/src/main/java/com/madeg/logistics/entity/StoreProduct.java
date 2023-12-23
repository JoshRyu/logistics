package com.madeg.logistics.entity;

import com.madeg.logistics.domain.StoreProductPatch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "store_product")
public class StoreProduct {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "store_product_id_seq"
  )
  @SequenceGenerator(
    name = "store_product_id_seq",
    sequenceName = "store_product_id_seq",
    allocationSize = 1
  )
  @Column(name = "store_product_id", unique = true, nullable = false)
  private Long storeProductId;

  @ManyToOne
  @JoinColumn(
    name = "store_code",
    referencedColumnName = "store_code",
    nullable = false
  )
  private Store store;

  @ManyToOne
  @JoinColumn(
    name = "product_code",
    referencedColumnName = "product_code",
    nullable = false
  )
  private Product product;

  @NotNull
  @Column(name = "store_price")
  private BigDecimal storePrice;

  @Column(name = "income_cnt")
  private Integer incomeCnt;

  @Column(name = "stock_cnt")
  private Integer stockCnt;

  @Column(name = "defect_cnt")
  private Integer defectCnt;

  @Column(name = "description")
  private String description;

  public void updateStorePrice(BigDecimal storePrice) {
    this.storePrice = storePrice;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public boolean isStateChanged(StoreProductPatch patchInput) {
    return (
      (
        patchInput.getStorePrice() == null ||
        (
          storePrice != null &&
          patchInput.getStorePrice() != null &&
          storePrice.compareTo(patchInput.getStorePrice()) != 0
        )
      ) ||
      !Objects.equals(description, patchInput.getDescription())
    );
  }
}
