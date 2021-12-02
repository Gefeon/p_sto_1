package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceAnswerControllerTest extends AbstractTestControllerClass{

    private final String url = "/api/user/question/100/answer/100";

    @Test
    @DataSet(value = {"dataset/DBAnswerExampleAnotherId.yml", USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = "dataset/expected/DBAnswerExampleAnotherId.yml")
    public void deleteAnswerWithIncorrectId_returnBadRequest() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mockMvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mockMvc.perform(delete(url).header(AUTH_HEADER, PREFIX + token.getToken()));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/DBAnswerExample.yml", USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {"dataset/expected/empty.yml"})
    public void deleteAnswer_returnStatusOk_AnswerDeleted() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mockMvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);
        ResultActions response = mockMvc.perform(delete(url).header(AUTH_HEADER, PREFIX + token.getToken()));
        response.andExpect(status().isOk());
    }

}