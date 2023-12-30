package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.domain.UserRes;
import com.madeg.logistics.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "User")
@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Operation(summary = "Login with username and password")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = UserLogin.class))
  )
  @PostMapping("/login")
  public ResponseEntity<Object> login(
    @RequestBody @Valid UserLoginInput loginInfo
  ) {
    return new ResponseEntity<>(
      userService.userLogin(loginInfo),
      HttpStatus.ACCEPTED
    );
  }

  @Operation(summary = "Register new User")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = UserLogin.class))
  )
  @PostMapping
  public ResponseEntity<Object> create(
    @RequestBody @Valid UserInput userInput
  ) {
    try {
      userService.createUser(userInput);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new CommonRes(HttpStatus.CREATED.value(), "USER IS CREATED"));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Get All User List")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = List.class))
  )
  @GetMapping
  public ResponseEntity<UserRes> getUserList() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        new UserRes(
          HttpStatus.OK.value(),
          "USERS ARE RETRIEVED",
          userService.getUsers()
        )
      );
  }

  @Operation(summary = "Update a Specific User by Id")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @PatchMapping("/{id}")
  public ResponseEntity<Object> patch(
    @PathVariable(name = "id", required = true) Long id,
    @RequestBody @Valid UserPatch patchInfo
  ) {
    try {
      userService.patchUser(id, patchInfo);
      return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(new CommonRes(HttpStatus.ACCEPTED.value(), "USER IS UPDATED"));
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @Operation(summary = "Delete a Specific User by Id")
  @ApiResponse(
    content = @Content(schema = @Schema(implementation = CommonRes.class))
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(
    @PathVariable(name = "id", required = true) Long id
  ) {
    try {
      userService.deleteUser(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
