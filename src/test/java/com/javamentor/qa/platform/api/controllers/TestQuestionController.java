package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestQuestionController extends AbstractTestApi {
    private final String URL= "/api/user/question/count";
    private static final String QUESTION = "dataset/QuestionController/Question.yml";
    private static final String USER_ENTITY = "dataset/QuestionController/user.yml";
    private static final String ROLE_ENTITY = "dataset/QuestionController/role.yml";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {QUESTION, USER_ENTITY,ROLE_ENTITY}, disableConstraints = true)
    public void countShouldBeThree() throws Exception {

        ResultActions response = mvc.perform(get(URL).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("3"));

    }


}
