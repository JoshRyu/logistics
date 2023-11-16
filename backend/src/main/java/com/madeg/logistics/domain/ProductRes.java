package com.madeg.logistics.domain;

import com.madeg.logistics.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class ProductRes extends CommonRes {

  private Page<Product> product;

  public ProductRes(int status, String message, Page<Product> product) {
    super(status, message);
    this.product = product;
  }
}
