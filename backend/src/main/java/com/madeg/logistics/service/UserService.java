package com.madeg.logistics.service;

import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.domain.UserLoginRes;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.domain.UserRefreshRes;
import com.madeg.logistics.entity.User;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.enums.Role;
import com.madeg.logistics.repository.UserRepository;
import com.madeg.logistics.util.JwtUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  public UserLoginRes userLogin(UserLoginInput loginInfo) {
    User user = userRepository.findByUsername(loginInfo.getUserName());

    if (user == null) {
      throw new ResponseStatusException(ResponseCode.NOT_FOUND.getStatus(), ResponseCode.NOT_FOUND.getMessage("사용자"));
    }

    if (passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {

      UserLogin userLogin = UserLogin.builder().userName(loginInfo.getUserName()).password(loginInfo.getPassword())
          .role(user.getRole()).accessToken(jwtUtil.generateAccessToken(
              user.getUsername(),
              user.getRole()))
          .refreshToken(jwtUtil.generateRefreshToken(user.getUsername())).build();

      return new UserLoginRes(ResponseCode.RETRIEVED.getCode(),
          ResponseCode.RETRIEVED.getMessage("로그인 정보"), userLogin);
    }

    throw new ResponseStatusException(
        ResponseCode.BAD_REQUEST.getStatus(),
        ResponseCode.BAD_REQUEST.getMessage("유효하지 않은 비밀번호입니다"));
  }

  public UserRefreshRes refreshAccessToken(String refreshToken) {

    String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);

    return new UserRefreshRes(ResponseCode.RETRIEVED.getCode(),
        ResponseCode.RETRIEVED.getMessage("Access 토큰 정보"), newAccessToken);
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public void createUser(UserInput userInput) {
    User previousUser = userRepository.findByUsername(userInput.getUsername());
    if (previousUser != null) {
      throw new ResponseStatusException(
          ResponseCode.CONFLICT.getStatus(),
          ResponseCode.CONFLICT.getMessage("사용자"));
    }

    Role userRole = Role.USER;

    if (userInput.getRole() != null) {
      userRole = userInput.getRole();
    }

    User user = User
        .builder()
        .username(userInput.getUsername())
        .password(passwordEncoder.encode(userInput.getPassword()))
        .role(userRole)
        .build();

    userRepository.save(user);
  }

  public void patchUser(Long id, UserPatch patchInput) {
    User previousUser = userRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(ResponseCode.NOT_FOUND.getStatus(),
            ResponseCode.NOT_FOUND.getMessage("사용자")));

    if (patchInput.getPassword() != null) {
      previousUser.setPassword(
          passwordEncoder.encode(patchInput.getPassword()));
    }

    if (patchInput.getRole() != null) {
      if (previousUser.getRole().equals(Role.ADMIN) &&
          patchInput.getRole() == Role.USER &&
          userRepository.countByRole(Role.ADMIN.name()) == 1) {
        throw new ResponseStatusException(
            ResponseCode.BAD_REQUEST.getStatus(),
            ResponseCode.BAD_REQUEST.getMessage("유일한 관리자 권한의 사용자의 권한을 바꿀 수 없습니다"));
      }
      previousUser.setRole(patchInput.getRole());
    }

    userRepository.save(previousUser);
  }

  public void deleteUser(Long id) {
    User previousUser = userRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(ResponseCode.NOT_FOUND.getStatus(),
            ResponseCode.NOT_FOUND.getMessage("사용자")));

    if (previousUser.getRole().equals(Role.ADMIN) &&
        userRepository.countByRole(Role.ADMIN.name()) == 1) {
      throw new ResponseStatusException(
          ResponseCode.BAD_REQUEST.getStatus(),
          ResponseCode.BAD_REQUEST.getMessage("유일한 관리자 권한의 사용자를 삭제할 수 없습니다"));
    }

    userRepository.delete(previousUser);
  }
}
