package com.madeg.logistics.domain;

import com.madeg.logistics.entity.SalesHistory;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesHistoryRes extends CommonRes {

  private List<SalesHistory> content;
  private SimplePageInfo pageable;

  public SalesHistoryRes(
    int status,
    String message,
    List<SalesHistory> content,
    SimplePageInfo pageable
  ) {
    super(status, message);
    this.content = content;
    this.pageable = pageable;
  }
}
