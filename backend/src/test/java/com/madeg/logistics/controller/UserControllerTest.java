package com.madeg.logistics.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.service.UserService;
import com.madeg.logistics.util.TestUtil;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@DisplayName("사용자 테스트")
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String LOGIN_API_URL = "/api/v1/user/login";

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("로그인 성공")
    public void loginSuccess() throws Exception {
        String requestBodyJson = objectMapper.writeValueAsString(new UserLoginInput("admin", "admin"));
        TestUtil.performRequest(mockMvc, LOGIN_API_URL, requestBodyJson, "POST", 200, 200);
    }

}
