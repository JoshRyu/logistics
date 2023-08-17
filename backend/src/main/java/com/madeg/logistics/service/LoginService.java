package com.madeg.logistics.service;

import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.entity.User;
import com.madeg.logistics.enums.Role;
import com.madeg.logistics.repository.UserRepository;
import com.madeg.logistics.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserLogin userLogin(UserLogin loginInfo) {
    User user = userRepository.findByUsername(loginInfo.getUsername());

    if (user == null) {
      throw new ResponseStatusException(
        HttpStatusCode.valueOf(404),
        "USER NOT FOUND"
      );
    }

    if (passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
      loginInfo.setRole(Role.valueOf(user.getRole()));
      loginInfo.setToken(jwtUtil.generateToken(user.getUsername()));

      return loginInfo;
    }

    throw new ResponseStatusException(
      HttpStatusCode.valueOf(400),
      "INVALID PASSWORD"
    );
  }
}
