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

  @ManyToOne
  @JoinColumn(
    name = "product_code",
    referencedColumnName = "product_code",
    nullable = false
  )
  private Product product;

  @Column(name = "month")
  private String month;

  @Column(name = "month_revenue")
  private BigDecimal monthRevenue;

  @Column(name = "month_profit")
  private BigDecimal monthProfit;
}
