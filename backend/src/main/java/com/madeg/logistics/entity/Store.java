package com.madeg.logistics.entity;

import com.madeg.logistics.enums.StoreType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "store")
public class Store {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "custom_sequence"
  )
  @GenericGenerator(
    name = "custom_sequence",
    strategy = "com.madeg.logistics.entity.CustomSequenceGenerator",
    parameters = { @Parameter(name = "prefix", value = "store_code_") }
  )
  @Column(name = "store_code", unique = true, nullable = false)
  private String storeCode;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "address")
  private String address;

  @Column(name = "type", nullable = false)
  private StoreType type;

  @Column(name = "fixed_cost")
  private Integer fixedCost;

  @Column(name = "commission_rate")
  private Double commissionRate;

  @Column(name = "description")
  private String description;

  public void updateName(String name) {
    this.name = name;
  }

  public void updateAddress(String address) {
    this.address = address;
  }

  public void updateType(StoreType type) {
    this.type = type;
  }

  public void updateFixedCost(Integer fixedCost) {
    this.fixedCost = fixedCost;
  }

  public void updateComissionRate(Double commissionRate) {
    this.commissionRate = commissionRate;
  }

  public void updateDescription(String description) {
    this.description = description;
  }
}
