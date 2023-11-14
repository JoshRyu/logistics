package com.madeg.logistics.domain;

import com.madeg.logistics.entity.Product;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRes extends CommonRes {

  private List<Product> product;

  public ProductRes(int status, String message, List<Product> product) {
    super(status, message);
    this.product = product;
  }
}
