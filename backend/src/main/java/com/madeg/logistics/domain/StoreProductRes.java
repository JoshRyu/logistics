package com.madeg.logistics.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreProductRes extends CommonRes {

  private List<StoreProductOutput> content;
  private SimplePageInfo pageable;

  public StoreProductRes(
    int status,
    String message,
    List<StoreProductOutput> content,
    SimplePageInfo pageable
  ) {
    super(status, message);
    this.content = content;
    this.pageable = pageable;
  }
}
