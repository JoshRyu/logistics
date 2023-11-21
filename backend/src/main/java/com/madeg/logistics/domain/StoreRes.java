package com.madeg.logistics.domain;

import com.madeg.logistics.entity.Store;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRes extends CommonRes {

  private List<Store> store;

  public StoreRes(int status, String message, List<Store> store) {
    super(status, message);
    this.store = store;
  }
}
