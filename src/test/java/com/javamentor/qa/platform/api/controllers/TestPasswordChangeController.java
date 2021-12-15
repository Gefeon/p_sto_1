package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestPasswordChangeController extends AbstractTestApi {

    private final String url = "/api/user/changePassword";

    private static final String USER_ENTITY = "dataset/answerResourceController/user.yml";
    private static final String ROLE_ENTITY = "dataset/answerResourceController/role.yml";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final String CORRECT_PASS = "12345q";
    private static final String TOO_SHORT_PASS = "12345";
    private static final String TOO_LONG_PASS = "12345qwertyui";
    private static final String BLANC_PASS = "      ";
    private static final String WRONG_CHARSET_PASS = "12345вася";

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void changePassword() throws Exception {
        String authHeader = PREFIX + getToken("user100@user.ru", "user");

        ResultActions response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).param("password", CORRECT_PASS));
        response.andExpect(status().isOk());

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).param("password", TOO_SHORT_PASS));
        response.andExpect(status().isBadRequest());

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).param("password", TOO_LONG_PASS));
        response.andExpect(status().isBadRequest());

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).param("password", BLANC_PASS));
        response.andExpect(status().isBadRequest());

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).param("password", WRONG_CHARSET_PASS));
        response.andExpect(status().isBadRequest());
    }

}