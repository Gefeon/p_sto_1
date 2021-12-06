package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestTagController extends AbstractTestApi {
    private static final String getIgnoredUrl = "/api/user/tag/ignored";
    private static final String USER_ENTITY = "dataset/tagController/user.yml";
    private static final String TAG_ENTITY = "dataset/tagController/tag.yml";
    private static final String IGNORED_TAG_ENTITY = "dataset/tagController/ignoredTag.yml";
    private static final String OTHER_USER_IGNORED_TAG_ENTITY = "dataset/tagController/otherUserIgnoredTag.yml";
    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {USER_ENTITY, TAG_ENTITY, IGNORED_TAG_ENTITY}, disableConstraints = true)
    public void getAllIgnoredTags_returnStatusOkAndCorrectTags() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(get(getIgnoredUrl).header(AUTH_HEADER, PREFIX + token.getToken()));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(100, 101, 104)))
                .andExpect(jsonPath("$[0].description", nullValue()));
    }

    @Test
    @DataSet(value = {USER_ENTITY, TAG_ENTITY, OTHER_USER_IGNORED_TAG_ENTITY}, disableConstraints = true)
    public void NoUserRelated_returnEmptyArray() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(get(getIgnoredUrl).header(AUTH_HEADER, PREFIX + token.getToken()));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(0)));
    }
    @Test
    @DataSet(value = {USER_ENTITY}, disableConstraints = true)
    public void NoTagsInBD_returnEmptyArray() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(get(getIgnoredUrl).header(AUTH_HEADER, PREFIX + token.getToken()));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(0)));
    }

}
