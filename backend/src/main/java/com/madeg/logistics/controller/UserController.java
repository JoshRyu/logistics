package com.madeg.logistics.controller;

import com.madeg.logistics.domain.ResponseCommon;
import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.entity.User;
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
import org.springframework.validation.Errors;
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
    @RequestBody @Valid UserLoginInput loginInfo,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          new ResponseCommon(
            HttpStatus.BAD_REQUEST.value(),
            errors.getFieldError().getDefaultMessage()
          )
        );
    }
    return new ResponseEntity<>(
      userService.userLogin(loginInfo),
      HttpStatus.ACCEPTED
    );
  }

  @PostMapping
  public ResponseEntity<Object> create(
    @RequestBody @Valid UserInput userInput,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          new ResponseCommon(
            HttpStatus.BAD_REQUEST.value(),
            errors.getFieldError().getDefaultMessage()
          )
        );
    }
    try {
      userService.createUser(userInput);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
          new ResponseCommon(HttpStatus.CREATED.value(), "USER IS PATCHED")
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
    }
  }

  @GetMapping("/list")
  public List<User> getUserList() {
    return userService.getUsers();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> patch(
    @PathVariable(name = "id", required = true) Long id,
    @RequestBody @Valid UserPatch patchInfo,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          new ResponseCommon(
            HttpStatus.BAD_REQUEST.value(),
            errors.getFieldError().getDefaultMessage()
          )
        );
    }

    try {
      userService.patchUser(id, patchInfo);
      return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(
          new ResponseCommon(HttpStatus.ACCEPTED.value(), "USER IS UPDATED")
        );
    } catch (ResponseStatusException ex) {
      return ResponseEntity
        .status(ex.getStatusCode())
        .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
    }
  }

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
        .body(new ResponseCommon(ex.getStatusCode().value(), ex.getReason()));
    }
  }
}
