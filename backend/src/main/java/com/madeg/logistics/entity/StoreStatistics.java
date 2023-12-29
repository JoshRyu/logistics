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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "store_statistics")
public class StoreStatistics {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "statistics_id_seq"
  )
  @SequenceGenerator(
    name = "statistics_id_seq",
    sequenceName = "statistics_id_seq",
    allocationSize = 1
  )
  @Column(name = "statistics_id", unique = true, nullable = false)
  private Long statisticsId;

  @ManyToOne
  @JoinColumn(
    name = "store_code",
    referencedColumnName = "store_code",
    nullable = false
  )
  private Store store;

  @Column(name = "month")
  private String month;

  @Column(name = "month_revenue")
  private BigDecimal monthRevenue;

  @Column(name = "month_profit")
  private BigDecimal monthProfit;

  public void updateMonthRevenue(BigDecimal monthRevenue) {
    this.monthRevenue = monthRevenue;
  }

  public void updateMonthProfit(BigDecimal monthProfit) {
    this.monthProfit = monthProfit;
  }
}
