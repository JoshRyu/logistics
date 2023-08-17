package com.madeg.logistics.domain;

import com.madeg.logistics.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {

  private String username;
  private String password;
  private Role role;
  private String token;
}
