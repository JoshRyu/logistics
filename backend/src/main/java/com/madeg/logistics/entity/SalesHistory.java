package com.madeg.logistics.entity;

import com.madeg.logistics.domain.SalesHistoryPatch;
import jakarta.persistence.*;
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
@Table(name = "sales_history")
public class SalesHistory {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "sales_history_id_seq"
  )
  @SequenceGenerator(
    name = "sales_history_id_seq",
    sequenceName = "sales_history_id_seq",
    allocationSize = 1
  )
  @Column(name = "sales_id", unique = true, nullable = false)
  private Long salesId;

  @ManyToOne
  @JoinColumn(
    name = "store_product_id",
    referencedColumnName = "store_product_id",
    nullable = false
  )
  private StoreProduct storeProduct;

  @Column(name = "sales_month")
  private String salesMonth;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "sales_price", nullable = false)
  private BigDecimal salesPrice;

  @Column(name = "memo")
  private String memo;

  public void updateQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public void updateMemo(String memo) {
    this.memo = memo;
  }

  public boolean isStateChanged(SalesHistoryPatch patchInput) {
    return (
      !Objects.equals(quantity, patchInput.getQuantity()) ||
      !Objects.equals(memo, patchInput.getMemo())
    );
  }
}
