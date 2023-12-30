package com.madeg.logistics.domain;

import com.madeg.logistics.entity.StoreStatistics;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreStatisticsRes extends CommonRes {

  private List<StoreStatistics> content;
  private SimplePageInfo pageable;

  public StoreStatisticsRes(
    int status,
    String message,
    List<StoreStatistics> content,
    SimplePageInfo pageable
  ) {
    super(status, message);
    this.content = content;
    this.pageable = pageable;
  }
}
