package com.madeg.logistics.domain;

import com.madeg.logistics.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatch {

  @Pattern(
    regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
    message = "비밀번호는 문자, 숫자, 특수문자를 하나 이상 포함하며 8자 이상이어야 합니다."
  )
  @Schema(example = "Jennie456!")
  private String password;

  private Role role;
}
