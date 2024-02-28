package com.madeg.logistics.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madeg.logistics.domain.UserInput;
import com.madeg.logistics.domain.UserLoginInput;
import com.madeg.logistics.domain.UserPatch;
import com.madeg.logistics.enums.Role;
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

    @Autowired
    private Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String getAdminId() {
        return env.getProperty("account.admin.id");
    }

    private String getPassword() {
        return env.getProperty("account.admin.password");
    }

    private static final String USER_API_URL = "/api/v1/user";
    private static final String LOGIN_API_URL = "/api/v1/user/login";
    private static final String REFRESH_API_URL = "/api/v1/user/refresh";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("로그인 성공")
    public void loginSuccess() throws Exception {
        String requestBodyJson = objectMapper.writeValueAsString(new UserLoginInput(getAdminId(), getPassword()));
        TestUtil.performRequest(mockMvc, LOGIN_API_URL, requestBodyJson, "POST", 200, 200);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    public void loginFailInvalidPwd() throws Exception {
        String requestBodyJson = objectMapper.writeValueAsString(new UserLoginInput(getAdminId(), "INVALID_PWD"));
        TestUtil.performRequest(mockMvc, LOGIN_API_URL, requestBodyJson, "POST", 400, 400);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("사용자 등록 성공")
    public void createUserSuccess() throws Exception {
        UserInput userInput = new UserInput("newUser", "password123!", Role.USER);
        String requestBodyJson = objectMapper.writeValueAsString(userInput);
        TestUtil.performRequest(mockMvc, USER_API_URL, requestBodyJson, "POST", 201, 201);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/UserSetup.sql")
    @DisplayName("사용자 등록 실패 - 이미 존재하는 사용자")
    public void createUserFailExistingUser() throws Exception {
        UserInput userInput = new UserInput("user1", "password123!", Role.USER);
        String requestBodyJson = objectMapper.writeValueAsString(userInput);
        TestUtil.performRequest(mockMvc, USER_API_URL, requestBodyJson, "POST", 409, 409);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("액세스 토큰 갱신 성공")
    public void refreshAccessTokenSuccess() throws Exception {
        String requestBodyJson = objectMapper.writeValueAsString(new UserLoginInput(getAdminId(), getPassword()));
        MvcResult result = TestUtil.performRequest(mockMvc, LOGIN_API_URL, requestBodyJson, "POST", 200, 200);

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        String refreshToken = responseBody.path("loginInfo").path("refreshToken").asText();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(REFRESH_API_URL);
        uriBuilder.queryParam("refreshToken", refreshToken);

        TestUtil.performRequest(mockMvc, uriBuilder.toUriString(), null, "GET", 200,
                200);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("액세스 토큰 갱신 실패 - 유효하지 않은 리프레시 토큰")
    public void refreshAccessTokenFailInvalidToken() throws Exception {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(REFRESH_API_URL);
        uriBuilder.queryParam("refreshToken", "INVALID_TOKEN");
        TestUtil.performRequest(mockMvc, uriBuilder.toUriString(), null, "GET", 400,
                400);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("사용자 목록 조회 성공")
    public void getUserListSuccess() throws Exception {
        TestUtil.performRequest(mockMvc, USER_API_URL, null, "GET", 200, 200);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/UserSetup.sql")
    @DisplayName("사용자 정보 수정 성공")
    public void patchUserSuccess() throws Exception {
        UserPatch userPatch = new UserPatch("newPassword123!", Role.ADMIN);
        String requestBodyJson = objectMapper.writeValueAsString(userPatch);
        TestUtil.performRequest(mockMvc, USER_API_URL + "/" + getIdByName("user1"), requestBodyJson, "PATCH", 200, 200);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("사용자 정보 수정 실패 - 존재하지 않는 사용자")
    public void patchUserFailNonExistingUser() throws Exception {
        UserPatch userPatch = new UserPatch("newPassword123!", Role.ADMIN);
        String requestBodyJson = objectMapper.writeValueAsString(userPatch);
        TestUtil.performRequest(mockMvc, USER_API_URL + "/-1", requestBodyJson, "PATCH", 404, 404);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("사용자 정보 수정 실패 - 유일한 관리자 권한 변경 시도")
    public void patchUserFailLastAdmin() throws Exception {
        UserPatch userPatch = new UserPatch("newPassword123!", Role.USER);
        String requestBodyJson = objectMapper.writeValueAsString(userPatch);
        TestUtil.performRequest(mockMvc, USER_API_URL + "/1", requestBodyJson, "PATCH", 400, 400);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/UserSetup.sql")
    @DisplayName("사용자 삭제 성공")
    public void deleteUserSuccess() throws Exception {
        TestUtil.performRequest(mockMvc, USER_API_URL + "/" + getIdByName("user1"), null, "DELETE", 204, 204);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/UserSetup.sql")
    @DisplayName("사용자 삭제 실패 - 유일한 관리자 삭제 시도")
    public void deleteUserFailLastAdmin() throws Exception {
        TestUtil.performRequest(mockMvc, USER_API_URL + "/1", null, "DELETE", 400, 400);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Long getIdByName(String name) {
        return jdbcTemplate.queryForObject("SELECT id FROM member WHERE username = ?", Long.class, name);
    }

}
