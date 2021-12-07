package com.javamentor.qa.platform.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestResourceTagController extends AbstractTestApi {

    private static final String QUESTION = "dataset/resourceTagController/Question.yml";
    private static final String TAG = "dataset/resourceTagController/Tag.yml";
    private static final String QUESTION_HAS_TAG = "dataset/resourceTagController/QuestionHasTag.yml";
    private static final String USER_ENTITY = "dataset/resourceTagController/User.yml";
    private static final String ROLE_ENTITY = "dataset/resourceTagController/Role.yml";

    private static final String GET_RELATED_TAGS = "/api/user/tag/related";
    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DataSet(value = {QUESTION, TAG, QUESTION_HAS_TAG, USER_ENTITY, ROLE_ENTITY})
    public void getRelatedTags() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        mvc.perform(get(GET_RELATED_TAGS).header(AUTH_HEADER, PREFIX + token.getToken()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(status().isOk());
    }

}
