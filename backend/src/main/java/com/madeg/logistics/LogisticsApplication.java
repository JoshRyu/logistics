package com.madeg.logistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogisticsApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogisticsApplication.class, args);
  }
}
