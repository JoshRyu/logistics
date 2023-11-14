package com.madeg.logistics.domain;

import com.madeg.logistics.entity.Category;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRes extends CommonRes {

  private List<Category> category;

  public CategoryRes(int status, String message, List<Category> category) {
    super(status, message);
    this.category = category;
  }
}
