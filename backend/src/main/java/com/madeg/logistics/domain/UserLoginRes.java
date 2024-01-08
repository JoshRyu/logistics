package com.madeg.logistics.domain;

import com.madeg.logistics.enums.Role;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRes extends CommonRes {

  @NotBlank(message = "사용자 명은 반드시 있어야 합니다.")
  private String userName;

  @NotBlank(message = "비밀번호는 반드시 있어야 합니다.")
  private String password;

  private Role role;

  private String accessToken;
  private String refreshToken;

  public UserLoginRes(int status, String message, String userName, String password, Role role, String accessToken,
      String refreshToken) {
    super(status, message);
    this.userName = userName;
    this.password = password;
    this.role = role;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
