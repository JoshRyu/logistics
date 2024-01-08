package com.madeg.logistics.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRes extends CommonRes {

  private UserLogin loginInfo;

  public UserLoginRes(int status, String message, UserLogin loginInfo) {
    super(status, message);
    this.loginInfo = loginInfo;
  }
}
