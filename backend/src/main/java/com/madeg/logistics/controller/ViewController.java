package com.madeg.logistics.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ViewController implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/user/**").setViewName("forward:/index.html");

    registry
        .addViewController("/product/**")
        .setViewName("forward:/index.html");

    registry
        .addViewController("/{path:[^\\.]*}")
        .setViewName("forward:/index.html");
  }
}
