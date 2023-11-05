package com.madeg.logistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "store")
public class Store {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "store_code_seq"
  )
  @SequenceGenerator(
    name = "store_code_seq",
    sequenceName = "store_code_seq",
    allocationSize = 1
  )
  @Column(name = "store_code", unique = true, nullable = false)
  private Long storeCode;

  @Column(name = "name")
  private String name;

  @Column(name = "address")
  private String address;

  @Column(name = "type")
  private String type;

  @Column(name = "fixed_cost")
  private BigDecimal fixedCost;

  @Column(name = "commission_rate")
  private BigDecimal commissionRate;

  @Column(name = "description")
  private String description;
}
