package com.madeg.logistics.security;

import com.madeg.logistics.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public void doFilter(
    ServletRequest servletRequest,
    ServletResponse servletResponse,
    FilterChain filterChain
  ) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    String token = jwtUtil.resolveToken(request);
    request.setAttribute("token", token);

    if (token != null && jwtUtil.validateToken(token)) {
      Authentication auth = jwtUtil.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, servletResponse);
  }
}
