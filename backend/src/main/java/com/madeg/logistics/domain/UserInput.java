package com.madeg.logistics.domain;

import com.madeg.logistics.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInput {

  @NotBlank(message = "사용자 명은 반드시 있어야 합니다.")
  @Pattern(regexp = "^.{2,19}$", message = "사용자 명은 2자 이상 20자 미만이어야 합니다.")
  @Schema(example = "Jennie")
  private String username;

  @NotBlank(message = "비밀번호는 반드시 있어야 합니다.")
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 문자, 숫자, 특수문자를 하나 이상 포함하며 8자 이상이어야 합니다.")
  @Schema(example = "Jennie123!")
  private String password;

  @Schema(example = "USER")
  private Role role;
}
