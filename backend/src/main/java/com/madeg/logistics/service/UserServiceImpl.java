package com.madeg.logistics.service;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLogin;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.domain.UserLoginRes;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.domain.UserRefreshRes;
import com.madeg.logistics.entity.User;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.enums.Role;
import com.madeg.logistics.repository.UserRepository;
import com.madeg.logistics.util.JwtUtil;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = new ModelMapper();
    this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
  }

  @Override
  public UserLoginRes userLogin(UserLoginInput loginInfo) {
    User user = userRepository.findByUsername(loginInfo.getUserName());

    if (user == null) {
      return new UserLoginRes(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage("사용자"), null);
    }

    if (!passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
      return new UserLoginRes(
          ResponseCode.BAD_REQUEST.getCode(),
          ResponseCode.BAD_REQUEST.getMessage("유효하지 않은 비밀번호입니다"), null);
    }

    UserLogin userLogin = modelMapper.map(user, UserLogin.class);
    userLogin.setAccessToken(jwtUtil.generateAccessToken(user.getUsername(), user.getRole()));
    userLogin.setRefreshToken(jwtUtil.generateRefreshToken(user.getUsername()));

    return new UserLoginRes(ResponseCode.RETRIEVED.getCode(),
        ResponseCode.RETRIEVED.getMessage("로그인 정보"),
        userLogin);
  }

  @Override
  public UserRefreshRes refreshAccessToken(String refreshToken) {
    try {
      String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);
      return new UserRefreshRes(ResponseCode.RETRIEVED.getCode(),
          ResponseCode.RETRIEVED.getMessage("Access 토큰 정보"), newAccessToken);
    } catch (Exception e) {
      return new UserRefreshRes(ResponseCode.BAD_REQUEST.getCode(),
          ResponseCode.BAD_REQUEST.getMessage("유효하지 않은 Refresh Token 입니다"), null);
    }
  }

  @Override
  public CommonRes createUser(UserInput userInput) {
    if (userRepository.findByUsername(userInput.getUsername()) != null) {
      return new CommonRes(
          ResponseCode.CONFLICT.getCode(),
          ResponseCode.CONFLICT.getMessage("사용자"));
    }

    User user = modelMapper.map(userInput, User.class);
    user.setPassword(passwordEncoder.encode(userInput.getPassword()));
    user.setRole(userInput.getRole() != null ? userInput.getRole() : Role.USER);

    userRepository.save(user);
    return new CommonRes(ResponseCode.CREATED.getCode(), ResponseCode.CREATED.getMessage("사용자"));
  }

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public CommonRes patchUser(Long id, UserPatch patchInput) {
    User user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return new CommonRes(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage("사용자"));
    }

    if (patchInput.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(patchInput.getPassword()));
    }

    if (patchInput.getRole() != null) {
      if (user.getRole() == Role.ADMIN && patchInput.getRole() == Role.USER
          && userRepository.countByRole(Role.ADMIN) == 1) {

        return new CommonRes(
            ResponseCode.BAD_REQUEST.getCode(),
            ResponseCode.BAD_REQUEST.getMessage("유일한 관리자 권한을 다른 사용자로 변경할 수 없습니다"));
      }
      user.setRole(patchInput.getRole());
    }

    userRepository.save(user);
    return new CommonRes(ResponseCode.UPDATED.getCode(), ResponseCode.UPDATED.getMessage("사용자"));

  }

  @Override
  public CommonRes deleteUser(Long id) {
    User user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return new CommonRes(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage("사용자"));
    }

    if (user.getRole() == Role.ADMIN &&
        userRepository.countByRole(Role.ADMIN) == 1) {
      return new CommonRes(
          ResponseCode.BAD_REQUEST.getCode(),
          ResponseCode.BAD_REQUEST.getMessage("유일한 관리자 권한의 사용자를 삭제할 수 없습니다"));
    }

    userRepository.delete(user);
    return new CommonRes(ResponseCode.DELETED.getCode(), ResponseCode.DELETED.getMessage("사용자"));
  }

}
