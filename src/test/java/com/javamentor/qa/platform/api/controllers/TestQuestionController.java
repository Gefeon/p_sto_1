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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestQuestionController extends AbstractTestApi {
    private final String URL= "/api/user/question/count";
    private static final String QUESTION = "dataset/QuestionController/Question.yml";
    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {QUESTION}, disableConstraints = true)
    public void countShouldBePositive() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);
        ResultActions response = mvc.perform(get(URL).header(AUTH_HEADER, PREFIX + token.getToken()));
               // .content(objectMapper.writeValueAsString(questionCreateDto))
                //.contentType(MediaType.APPLICATION_JSON));

        mvc.perform(get(URL)).andDo(print())
                .andExpect(content().json("16"))
                .andExpect(status().isOk());

    }


}
