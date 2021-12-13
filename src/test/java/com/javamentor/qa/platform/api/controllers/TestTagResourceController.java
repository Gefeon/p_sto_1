package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestTagResourceController extends AbstractTestApi {

    private static final String QUESTION = "dataset/TagResourceController/relatedTags/Question.yml";
    private static final String TAG = "dataset/TagResourceController/relatedTags/Tag.yml";
    private static final String QUESTION_HAS_TAG = "dataset/TagResourceController/relatedTags/QuestionHasTag.yml";
    private static final String USER_ENTITY = "dataset/TagResourceController/relatedTags/User.yml";
    private static final String ROLE_ENTITY = "dataset/TagResourceController/relatedTags/Role.yml";
    private static final String TAG_ENTITY = "dataset/TagResourceController/ignoredTags/Tag.yml";
    private static final String EMPTY = "dataset/TagResourceController/ignoredTags/Empty.yml";
    private static final String IGNORED_TAG_ENTITY = "dataset/TagResourceController/ignoredTags/IgnoredTag.yml";
    private static final String OTHER_USER_IGNORED_TAG_ENTITY = "dataset/TagResourceController/ignoredTags/OtherUserIgnoredTag.yml";

    private static final String GET_RELATED_TAGS = "/api/user/tag/related";
    private static final String GET_IGNORED_TAGS = "/api/user/tag/ignored";
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
    @DataSet(value = {USER_ENTITY, TAG_ENTITY, IGNORED_TAG_ENTITY}, disableConstraints = true)
    public void getAllIgnoredTags_returnStatusOkAndCorrectTags() throws Exception {
        ResultActions response = mvc.perform(get(GET_IGNORED_TAGS).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(100, 101, 104)))
                .andExpect(jsonPath("$[0].description", nullValue()));
    }

    @Test
    @DataSet(value = {USER_ENTITY, TAG_ENTITY, OTHER_USER_IGNORED_TAG_ENTITY}, disableConstraints = true)
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

}
