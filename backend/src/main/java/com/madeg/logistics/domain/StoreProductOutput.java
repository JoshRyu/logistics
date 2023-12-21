package com.madeg.logistics.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreProductOutput {

  private String productCode;

  private String productName;

  private BigDecimal storePrice;

  private Integer incomeCnt;

  private Integer defectCnt;

  private String description;
}
