package com.madeg.logistics.config;

import com.madeg.logistics.security.JwtAuthenticationFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthEntryPoint jwtAuthEntryPoint;
  private final JwtAuthenticationFilter jwtFilter;

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
      Arrays.asList("http://localhost:3500", "http://localhost:11101")
    );
    configuration.addAllowedHeader("*");
    configuration.setAllowedMethods(
      Arrays.asList("GET", "POST", "PATCH", "DELETE")
    );
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public DefaultSecurityFilterChain filterChain(HttpSecurity http)
    throws Exception {
    http
      .cors(cors -> corsConfigurationSource())
      .csrf(csrf -> csrf.disable())
      .exceptionHandling(req -> req.authenticationEntryPoint(jwtAuthEntryPoint))
      .authorizeHttpRequests(authorizeRequests ->
        authorizeRequests
          .requestMatchers(HttpMethod.POST, "/api/v1/user/login")
          .permitAll()
          .requestMatchers(HttpMethod.GET, "/api/v1/user/refresh")
          .permitAll()
          .requestMatchers(CorsUtils::isPreFlightRequest)
          .permitAll()
          // Allow Frontend Relevant Path
          .requestMatchers(
            "/",
            "/login",
            "/user/**",
            "/product/**",
            "/swagger-ui/*",
            "/api/swagger-config",
            "/api/logistics",
            "/index.html",
            "/favicon.ico",
            "/assets/**",
            "/favicon.ico"
          )
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
