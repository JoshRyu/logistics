package com.madeg.logistics.config;

import com.madeg.logistics.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

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
    http
      .authorizeHttpRequests(authorizeRequests ->
        authorizeRequests
          .requestMatchers(HttpMethod.OPTIONS, "/**/*")
          .permitAll()
          .requestMatchers(CorsUtils::isPreFlightRequest)
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .csrf(csrf -> csrf.disable());

    http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
