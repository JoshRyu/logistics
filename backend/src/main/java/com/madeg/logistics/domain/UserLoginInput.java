package com.madeg.logistics.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginInput {

  @NotBlank(message = "사용자 명은 반드시 있어야 합니다.")
  @Schema(example = "admin")
  private String userName;

  @Schema(example = "admin")
  @NotBlank(message = "비밀번호는 반드시 있어야 합니다.")
  private String password;
}
