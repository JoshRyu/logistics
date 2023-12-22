package com.madeg.logistics.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplePageInfo {

  private boolean last;
  private int page;
  private int size;
  private int totalPages;
  private long totalElements;
}
