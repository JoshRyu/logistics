package com.madeg.logistics.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController implements ErrorController {

  // Redirect to index.html
  @GetMapping(value = "/{path:[^\\.]*}")
  public String redirect() {
    return "forward:/";
  }

  @GetMapping("/error")
  public String redirectOnError() {
    return "redirect:/";
  }
}
