package com.madeg.logistics.domain;

import com.madeg.logistics.entity.Product;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRes extends CommonRes {

  private List<Product> content;
  private SimplePageInfo pageable;

  @Getter
  @Setter
  public static class SimplePageInfo {

    private boolean last;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
  }

  public ProductRes(
    int status,
    String message,
    List<Product> content,
    SimplePageInfo pageable
  ) {
    super(status, message);
    this.content = content;
    this.pageable = pageable;
  }
}
