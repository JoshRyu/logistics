package com.madeg.logistics.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class TestUtil {
    public static MvcResult performRequest(MockMvc mockMvc, @NonNull String url, String requestBodyJson, String method,
            int expectedStatus, Integer expectedRetCode) throws Exception {
        ResultActions resultActions = null;
        RequestBuilder requestBuilder = null;
        requestBodyJson = requestBodyJson == null ? "" : requestBodyJson;

        switch (method) {
            case "GET":
                requestBuilder = MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON);
                break;
            case "POST":
                requestBuilder = MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson);
                break;
            case "PATCH":
                requestBuilder = MockMvcRequestBuilders.patch(url).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson);
                break;
            case "DELETE":
                requestBuilder = MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON);
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }

        resultActions = mockMvc.perform(requestBuilder);

        return checkExpectedVal(resultActions, expectedStatus, expectedRetCode);
    }

    private static MvcResult checkExpectedVal(ResultActions resultActions, int expectedStatus, Integer expectedRetCode)
            throws Exception {
        if (expectedStatus == 200) {
            resultActions.andExpect(status().isOk());
        } else {
            resultActions.andExpect(status().is(expectedStatus));
        }

        if (expectedRetCode != null) {
            resultActions.andExpect(jsonPath("$.status").value(expectedRetCode));
        }

        return resultActions.andReturn();
    }

}
