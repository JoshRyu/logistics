package com.madeg.logistics.domain;

import com.madeg.logistics.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserLogin {

  @NotBlank(message = "사용자 명은 반드시 있어야 합니다.")
  private String username;

  @NotBlank(message = "비밀번호는 반드시 있어야 합니다.")
  private String password;

  private Role role;
  private String token;
}
