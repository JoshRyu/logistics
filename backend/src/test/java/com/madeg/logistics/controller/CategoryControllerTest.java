package com.madeg.logistics.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madeg.logistics.domain.CategoryInput;
import com.madeg.logistics.domain.CategoryPatch;
import com.madeg.logistics.service.CategoryService;
import com.madeg.logistics.util.TestUtil;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@DisplayName("카테고리 테스트")
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String CATEGORY_API_URL = "/api/v1/category";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("카테고리 생성 성공")
    public void createCategorySuccess() throws Exception {
        String requestBodyJson = objectMapper.writeValueAsString(new CategoryInput("TEST_CATEGORY", null, "양털이 몽글몽글"));
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL, requestBodyJson, "POST", 201, 201);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 생성 실패 - 존재하는 카테고리 이름")
    public void createCategoryFailConflictCategoryName() throws Exception {
        String requestBodyJson = objectMapper.writeValueAsString(new CategoryInput("목도리", null, "양털이 몽글몽글"));
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL, requestBodyJson, "POST", 409, 409);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("카테고리 생성 실패 - 부모 카테고리를 찾을 수 없음")
    public void createCategoryFailParentNotFound() throws Exception {
        String requestBodyJson = objectMapper.writeValueAsString(new CategoryInput("목도리", "INVALID", "양털이 몽글몽글"));
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL, requestBodyJson, "POST", 404, 404);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("카테고리 조회 성공")
    public void getCategorySuccess() throws Exception {
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL, null, "GET", 200, 200);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 수정 성공")
    public void updateCategorySuccess() throws Exception {
        CategoryPatch categoryPatch = new CategoryPatch("목도리", "시즌 상품", null);
        String requestBodyJson = objectMapper.writeValueAsString(categoryPatch);
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/" + getCodeByName("목도리"), requestBodyJson, "PATCH", 200,
                200);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 수정 실패 - 중복되는 카테고리 이름")
    public void updateCategoryFailConflictCategoryName() throws Exception {
        CategoryPatch categoryPatch = new CategoryPatch("귀도리", "시즌 상품", null);
        String requestBodyJson = objectMapper.writeValueAsString(categoryPatch);
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/" + getCodeByName("목도리"), requestBodyJson, "PATCH", 409,
                409);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 수정 실패 - 부모 카테고리와 카테고리 코드가 동일")
    public void updateCategoryFailEqaulCode() throws Exception {
        CategoryPatch categoryPatch = new CategoryPatch("목도리", "시즌 상품", getCodeByName("목도리"));
        String requestBodyJson = objectMapper.writeValueAsString(categoryPatch);
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/" + getCodeByName("목도리"), requestBodyJson, "PATCH", 400,
                400);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 수정 실패 - 카테고리를 찾을 수 없음")
    public void updateCategoryFailPaNotFound() throws Exception {
        CategoryPatch categoryPatch = new CategoryPatch("목도리", "시즌 상품", "INVALID_PARENT_CATEGORY");
        String requestBodyJson = objectMapper.writeValueAsString(categoryPatch);
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/" + "INVALID_CATEGORY_CODE", requestBodyJson, "PATCH",
                404,
                404);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 수정 실패 - 부모 카테고리를 찾을 수 없음")
    public void updateCategoryFailParentNotFound() throws Exception {
        CategoryPatch categoryPatch = new CategoryPatch("목도리", "시즌 상품", "INVALID_PARENT_CATEGORY");
        String requestBodyJson = objectMapper.writeValueAsString(categoryPatch);
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/" + getCodeByName("목도리"), requestBodyJson, "PATCH", 404,
                404);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 수정 실패 - 변경 사항이 없음")
    public void updateCategoryFailunchanged() throws Exception {
        CategoryPatch categoryPatch = new CategoryPatch("목도리", "겨울 한정 상품", null);
        String requestBodyJson = objectMapper.writeValueAsString(categoryPatch);
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/" + getCodeByName("목도리"), requestBodyJson, "PATCH", 204,
                204);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 삭제 성공")
    public void deleteCategorySuccess() throws Exception {
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/" + getCodeByName("목도리"), null, "DELETE", 204, 204);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Sql("/CategorySetup.sql")
    @DisplayName("카테고리 삭제 실패 - 카테고리를 찾을 수 없음")
    public void deleteCategoryFailCategoryNotFound() throws Exception {
        TestUtil.performRequest(mockMvc, CATEGORY_API_URL + "/INVALID_CODE", null, "DELETE", 404, 404);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getCodeByName(String name) {
        return jdbcTemplate.queryForObject("SELECT category_code FROM category WHERE name = ?", String.class, name);
    }
}
