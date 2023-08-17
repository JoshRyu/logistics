package com.madeg.logistics.controller;

import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

  @Autowired
  private LoginService loginService;

  @PostMapping("/login")
  public UserLogin login(@RequestBody(required = true) UserLogin loginInfo) {
    return loginService.userLogin(loginInfo);
  }
}
