package com.madeg.logistics.service;

import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.entity.User;
import com.madeg.logistics.enums.Role;
import com.madeg.logistics.repository.UserRepository;
import com.madeg.logistics.util.JwtUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private static final String NOT_FOUND_MSG = "404: USER NOT_FOUND";

  public UserLogin userLogin(UserLogin loginInfo) {
    User user = userRepository.findByUsername(loginInfo.getUsername());

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    if (passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
      loginInfo.setRole(Role.valueOf(user.getRole()));
      loginInfo.setToken(jwtUtil.generateToken(user.getUsername(), Role.valueOf(user.getRole())));

      return loginInfo;
    }

    throw new ResponseStatusException(
      HttpStatus.BAD_REQUEST,
      "400: INVALID PASSWORD"
    );
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public void createUser(UserInput userInput) {
    User previousUser = userRepository.findByUsername(userInput.getUsername());
    if (previousUser != null) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "409: USER ALREADY EXIST"
      );
    }

    Role userRole = Role.USER;

    if (userInput.getRole() != null) {
      userRole = userInput.getRole();
    }

    User user = User
      .builder()
      .username(userInput.getUsername())
      .password(passwordEncoder.encode(userInput.getPassword()))
      .role(userRole.name())
      .build();

    userRepository.save(user);
  }

  public void patchUser(Long id, UserPatch patchInput) {
    User previousUser = userRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MSG)
      );

    if (patchInput.getPassword() != null) {
      previousUser.setPassword(
        passwordEncoder.encode(patchInput.getPassword())
      );
    }

    if (patchInput.getRole() != null) {
      if (
        previousUser.getRole().equals(Role.ADMIN.name()) &&
        patchInput.getRole() == Role.USER &&
        userRepository.countByRole(Role.ADMIN.name()) == 1
      ) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "400: CANNOT PATCH LAST ADMIN USER"
        );
      }
      previousUser.setRole(patchInput.getRole().name());
    }

    userRepository.save(previousUser);
  }

  public void deleteUser(Long id) {
    User previousUser = userRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MSG)
      );

    if (
      previousUser.getRole().equals(Role.ADMIN.name()) &&
      userRepository.countByRole(Role.ADMIN.name()) == 1
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "400: CANNOT DELETE LAST ADMIN USER"
      );
    }

    userRepository.delete(previousUser);
  }
}
