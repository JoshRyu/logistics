package com.madeg.logistics.config;

import com.madeg.logistics.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public JwtAuthenticationFilter jwtFilter() {
    return new JwtAuthenticationFilter();
  }

  @Bean
  public DefaultSecurityFilterChain filterChain(
    HttpSecurity http,
    JwtAuthenticationFilter jwtFilter
  ) throws Exception {
    http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    http.csrf(csrf -> csrf.disable());

    return http.build();
  }
}
