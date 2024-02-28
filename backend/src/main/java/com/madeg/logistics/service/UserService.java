package com.madeg.logistics.service;

import java.util.List;

import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.domain.UserLoginRes;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.domain.UserRefreshRes;
import com.madeg.logistics.entity.User;

public interface UserService {

    UserLoginRes userLogin(UserLoginInput loginInfo);

    UserRefreshRes refreshAccessToken(String refreshToken);

    List<User> getUsers();

    void createUser(UserInput userInput);

    void patchUser(Long id, UserPatch patchInput);

    void deleteUser(Long id);
}
