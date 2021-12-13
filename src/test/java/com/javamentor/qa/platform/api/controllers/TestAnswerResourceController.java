package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestAnswerResourceController extends AbstractTestApi {

    private final String url = "/api/user/question/100/answer/100";

    private static final String USER_ENTITY = "dataset/answerResourceController/user.yml";
    private static final String ROLE_ENTITY = "dataset/answerResourceController/role.yml";
    private static final String ANSWER_ENTITY = "dataset/answerResourceController/answer.yml";
    private static final String ANOTHER_ANSWER_ENTITY = "dataset/answerResourceController/anotherAnswer.yml";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {ANOTHER_ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {ANOTHER_ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY})
    public void deleteAnswerWithIncorrectId_returnBadRequest() throws Exception {
        ResultActions response = mvc.perform(delete(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {USER_ENTITY, ROLE_ENTITY})
    public void deleteAnswer_returnStatusOk_AnswerDeleted() throws Exception {
        ResultActions response = mvc.perform(delete(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk());
    }

}