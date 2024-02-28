package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLoginRes;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.domain.UserRefreshRes;
import com.madeg.logistics.domain.UserRes;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "User")
@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Login with username and password")
  @ApiResponse(content = @Content(schema = @Schema(implementation = UserLoginRes.class)))
  @PostMapping("/login")
  public ResponseEntity<UserLoginRes> login(
      @RequestBody @Valid UserLoginInput loginInfo) {

    UserLoginRes userLoginRes = userService.userLogin(loginInfo);
    return ResponseEntity.status(userLoginRes.getStatus())
        .body(userLoginRes);
  }

  @Operation(summary = "Get Access Token with Refresh Token")
  @ApiResponse(content = @Content(schema = @Schema(implementation = UserRefreshRes.class)))
  @GetMapping("/refresh")
  public ResponseEntity<UserRefreshRes> refreshAccessToken(@RequestParam("refreshToken") String refreshToken) {

    UserRefreshRes userRefreshRes = userService.refreshAccessToken(refreshToken);
    return ResponseEntity.status(userRefreshRes.getStatus())
        .body(userRefreshRes);

  }

  @Operation(summary = "Register new User")
  @ApiResponse(content = @Content(schema = @Schema(implementation = UserLoginRes.class)))
  @PostMapping
  public ResponseEntity<CommonRes> create(
      @RequestBody @Valid UserInput userInput) {
    try {
      CommonRes commonRes = userService.createUser(userInput);
      return ResponseEntity
          .status(commonRes.getStatus())
          .body(commonRes);
    } catch (Exception e) {
      return ResponseEntity
          .status(ResponseCode.INTERNAL_ERROR.getStatus())
          .body(new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage()));
    }
  }

  @Operation(summary = "Get All User List")
  @ApiResponse(content = @Content(schema = @Schema(implementation = List.class)))
  @GetMapping
  public ResponseEntity<UserRes> getUserList() {
    return ResponseEntity
        .status(ResponseCode.RETRIEVED.getStatus())
        .body(
            new UserRes(
                ResponseCode.RETRIEVED.getCode(),
                ResponseCode.RETRIEVED.getMessage("사용자 목록"),
                userService.getUsers()));
  }

  @Operation(summary = "Update a Specific User by Id")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @PatchMapping("/{id}")
  public ResponseEntity<CommonRes> patch(
      @PathVariable(name = "id", required = true) Long id,
      @RequestBody @Valid UserPatch patchInfo) {
    try {
      CommonRes commonRes = userService.patchUser(id, patchInfo);
      return ResponseEntity
          .status(commonRes.getStatus())
          .body(commonRes);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity
          .status(ResponseCode.INTERNAL_ERROR.getStatus())
          .body(new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage()));
    }
  }

  @Operation(summary = "Delete a Specific User by Id")
  @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
  @DeleteMapping("/{id}")
  public ResponseEntity<CommonRes> delete(
      @PathVariable(name = "id", required = true) Long id) {
    try {
      CommonRes commonRes = userService.deleteUser(id);
      return ResponseEntity.status(commonRes.getStatus()).body(commonRes);
    } catch (Exception e) {
      return ResponseEntity
          .status(ResponseCode.INTERNAL_ERROR.getStatus())
          .body(new CommonRes(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getMessage()));
    }
  }
}
