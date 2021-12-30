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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestTagResourceController extends AbstractTestApi {

    private static final String QUESTION = "dataset/tagResourceController/relatedTags/question.yml";
    private static final String TAG = "dataset/tagResourceController/relatedTags/tag.yml";
    private static final String SPECIAL_LETTERS_TAG = "dataset/tagResourceController/tagsByLetters/special_letters_tag.yml";
    private static final String QUESTION_HAS_TAG = "dataset/tagResourceController/relatedTags/question_has_tag.yml";
    private static final String USER_ENTITY = "dataset/tagResourceController/relatedTags/user.yml";
    private static final String ROLE_ENTITY = "dataset/tagResourceController/relatedTags/role.yml";
    private static final String TAG_ENTITY = "dataset/tagResourceController/ignoredTags/tag.yml";
    private static final String EMPTY = "dataset/tagResourceController/ignoredTags/empty.yml";
    private static final String IGNORED_TAG_ENTITY = "dataset/tagResourceController/ignoredTags/ignored_tag.yml";
    private static final String OTHER_USER_IGNORED_TAG_ENTITY = "dataset/tagResourceController/ignoredTags/other_user_ignored_tag.yml";
    private static final String TAG_ENTITY_TRACKED = "dataset/tagResourceController/trackedTags/tag.yml";
    private static final String TRACKED_TAG_ENTITY = "dataset/tagResourceController/trackedTags/tracked_tag.yml";

    private static final String GET_TRACKED_TAGS = "/api/user/tag/tracked";
    private static final String GET_RELATED_TAGS = "/api/user/tag/related";
    private static final String GET_IGNORED_TAGS = "/api/user/tag/ignored";
    private static final String GET_TAGS_BY_LETTERS = "/api/user/tag/letters";
    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {QUESTION, TAG, QUESTION_HAS_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getRelatedTags() throws Exception {
        mvc.perform(get(GET_RELATED_TAGS).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(status().isOk());
    }


    @Test
    @DataSet(value = {USER_ENTITY, TAG_ENTITY, IGNORED_TAG_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getAllIgnoredTags_returnStatusOkAndCorrectTags() throws Exception {
        ResultActions response = mvc.perform(get(GET_IGNORED_TAGS).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(100, 101, 104)))
                .andExpect(jsonPath("$[0].description", nullValue()));
    }


    @Test
    @DataSet(value = {USER_ENTITY, TAG_ENTITY, OTHER_USER_IGNORED_TAG_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getIgnoredTagsWithNoUserRelated_returnEmptyArray() throws Exception {
        ResultActions response = mvc.perform(get(GET_IGNORED_TAGS).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(0)));
    }



    @Test
    @DataSet(value = {EMPTY, USER_ENTITY}, disableConstraints = true)
    public void getIgnoredTagsWithNoTagsInBD_returnEmptyArray() throws Exception {
        ResultActions response = mvc.perform(get(GET_IGNORED_TAGS).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(0)));
    }


    @Test
    @DataSet(value = {USER_ENTITY, TAG_ENTITY_TRACKED, TRACKED_TAG_ENTITY}, disableConstraints = true)
    public void getAllTrackedTags() throws Exception {
        mvc.perform(get(GET_TRACKED_TAGS).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(100, 101, 102)))
                .andExpect(jsonPath("$[0].description", nullValue()));
    }

    @Test
    @DataSet(value = {TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getTagsByLetters_returnCorrectResult() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(post(GET_TAGS_BY_LETTERS)
                .header(AUTH_HEADER, PREFIX + token.getToken())
                .content("{\"letters\": \"en\"}")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("seven","ten","eleven","thirteen")));
    }

    @Test
    @DataSet(value = {SPECIAL_LETTERS_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getTagsByLetters_moreSixMatches_returnSixCorrectTags() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(post(GET_TAGS_BY_LETTERS)
                .header(AUTH_HEADER, PREFIX + token.getToken())
                .content("{\"letters\": \"se\"}")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        containsString("se"),
                        containsString("se"),
                        containsString("se"),
                        containsString("se"),
                        containsString("se"),
                        containsString("se"))));
    }
}