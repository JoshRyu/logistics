package com.madeg.logistics.controller;

import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.entity.User;
import com.madeg.logistics.service.UserService;
import com.madeg.logistics.util.ErrorUtil;
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

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private ErrorUtil errorUtil;

  @PostMapping("/login")
  public ResponseEntity<Object> login(
    @RequestBody @Valid UserLogin loginInfo,
    Errors errors
  ) {
    if (errors.hasErrors()) {
      return new ResponseEntity<>(
        errorUtil.getErrorMessages(errors),
        HttpStatus.BAD_REQUEST
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
      return new ResponseEntity<>(
        errorUtil.getErrorMessages(errors),
        HttpStatus.BAD_REQUEST
      );
    }

    userService.createUser(userInput);

    return new ResponseEntity<>(HttpStatus.CREATED);
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
      return new ResponseEntity<>(
        errorUtil.getErrorMessages(errors),
        HttpStatus.BAD_REQUEST
      );
    }
    userService.patchUser(id, patchInfo);

    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable(name = "id", required = true) Long id) {
    userService.deleteUser(id);
  }
}
