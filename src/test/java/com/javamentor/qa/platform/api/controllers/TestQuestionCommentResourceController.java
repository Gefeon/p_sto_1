package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestQuestionCommentResourceController extends AbstractTestApi {

    private final String urlComment = "/api/user/question/100/comment";

    private static final String USER_ENTITY = "dataset/QuestionResourceController/user.yml";
    private static final String ROLE_ENTITY = "dataset/QuestionResourceController/role.yml";
    private static final String COMMENT_ENTITY = "dataset/QuestionResourceController/Comment.yml";
    private static final String QUESTION_ENTITY = "dataset/QuestionResourceController/question.yml";
    private static final String REPUTATION_ENTITY = "dataset/QuestionResourceController/Reputation.yml";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {USER_ENTITY,
            ROLE_ENTITY,
            COMMENT_ENTITY,
            QUESTION_ENTITY,
            REPUTATION_ENTITY,
    }, disableConstraints = true)
    public void getQuestionCommentDtoById() throws Exception {

        String token = getToken("user100@user.ru", "user");

        mvc.perform(get(urlComment).header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", containsInAnyOrder(100)))
                .andExpect(jsonPath("$.[*].questionId", containsInAnyOrder(100)))
                .andExpect(jsonPath("$.[*].lastRedactionDate", containsInAnyOrder("2021-11-30T00:29:29")))
                .andExpect(jsonPath("$.[*].persistDate", containsInAnyOrder("2021-11-30T00:29:29")))
                .andExpect(jsonPath("$.[*].text", containsInAnyOrder("fix lazyInitialization Exception")))
                .andExpect(jsonPath("$.[*].userId", containsInAnyOrder(100)))
                .andExpect(jsonPath("$.[*].imageLink", containsInAnyOrder("test.ru")))
                .andExpect(jsonPath("$.[*].reputation", containsInAnyOrder(1)));

        /*
         * Проверка на не существующий ID
         * */
        mvc.perform(get("/api/user/question/1000/comment").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Missing comment or invalid ID"));
    }
}
