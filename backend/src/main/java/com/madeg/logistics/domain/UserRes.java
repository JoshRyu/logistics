package com.madeg.logistics.domain;

import com.madeg.logistics.entity.User;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRes extends CommonRes {

  private List<User> user;

  public UserRes(int status, String message, List<User> user) {
    super(status, message);
    this.user = user;
  }
}
