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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestTagResourceController extends AbstractTestApi {

    private static final String QUESTION = "dataset/tagResourceController/relatedTags/question.yml";
    private static final String TAG_ENTITY = "dataset/tagResourceController/allTags/tag.yml";
    private static final String SPECIAL_LETTERS_TAG = "dataset/tagResourceController/tagsByLetters/special_letters_tag.yml";
    private static final String QUESTION_HAS_TAG = "dataset/tagResourceController/relatedTags/question_has_tag.yml";
    private static final String USER_ENTITY = "dataset/tagResourceController/relatedTags/user.yml";
    private static final String ROLE_ENTITY = "dataset/tagResourceController/relatedTags/role.yml";
    private static final String EMPTY = "dataset/tagResourceController/ignoredTags/empty.yml";
    private static final String IGNORED_TAG_ENTITY = "dataset/tagResourceController/ignoredTags/ignored_tag.yml";
    private static final String OTHER_USER_IGNORED_TAG_ENTITY = "dataset/tagResourceController/ignoredTags/other_user_ignored_tag.yml";
    private static final String TRACKED_TAG_ENTITY = "dataset/tagResourceController/trackedTags/tracked_tag.yml";
    private static final String TAG_PERSIST_DATE = "dataset/tagResourceController/TagPaginationByDate/tagByDate.yml";
    private static final String QUESTION_BY_POPULAR = "dataset/tagResourceController/questionByPopular/question.yml";
    private static final String QUESTION_HAS_TAG_BY_POPULAR = "dataset/tagResourceController/questionByPopular/question_has_tag.yml";
    private static final String TAG_ENTITY_ADD_TRACKED_OR_IGNORED_TAG = "dataset/tagResourceController/addTrackedOrIgnoredTag/tags.yml";

    private static final String ADD_TRACKED_TAG = "/api/user/tag/5/tracked";
    private static final String ADD_IGNORED_TAG = "/api/user/tag/4/ignored";
    private static final String ADD_IGNORED_TAG_NOT_EXIST = "/api/user/tag/10/ignored";
    private static final String ADD_TRACKED_TAG_NOT_EXIST = "/api/user/tag/10/tracked";
    private static final String GET_TRACKED_TAGS = "/api/user/tag/tracked";
    private static final String GET_RELATED_TAGS = "/api/user/tag/related";
    private static final String GET_IGNORED_TAGS = "/api/user/tag/ignored";
    private static final String GET_TAGS_BY_LETTERS = "/api/user/tag/letters";
    private static final String GET_TAGS_BY_NAME = "/api/user/tag/name";
    private static final String GET_TAGS_BY_DATE = "/api/user/tag/new";
    private static final String GET_TAGS_BY_POPULAR = "/api/user/tag/popular";
    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {QUESTION, TAG_ENTITY, QUESTION_HAS_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
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
    @DataSet(value = {USER_ENTITY, TAG_ENTITY, TRACKED_TAG_ENTITY}, disableConstraints = true)
    public void getAllTrackedTags() throws Exception {
        mvc.perform(get(GET_TRACKED_TAGS).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(105, 106, 107)))
                .andExpect(jsonPath("$[0].description", nullValue()));
    }

    @Test
    @DataSet(value = {TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getTagsByLetters_returnCorrectResult() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(get(GET_TAGS_BY_LETTERS + "?letters=en")
                .header(AUTH_HEADER, PREFIX + token.getToken())
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("seven", "ten", "eleven", "thirteen")));
    }

    @Test
    @DataSet(value = {SPECIAL_LETTERS_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getTagsByLetters_moreSixMatches_returnSixCorrectTags() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(get(GET_TAGS_BY_LETTERS + "?letters=se")
                .header(AUTH_HEADER, PREFIX + token.getToken())
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

    /*
     * ???????? ?????????????????? TagDto ???? ??????????
     * */
    @Test
    @DataSet(value = {QUESTION, TAG_ENTITY, QUESTION_HAS_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getTagsPaginationByName() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // ?????????????????????? ????????????
        mvc.perform(get("/api/user/tag/name?currPage=1&items=3").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(5)))
                .andExpect(jsonPath("$.itemsOnPage", is(3)))
                .andExpect(jsonPath("$.totalResultCount", is(13)))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(107, 110, 104)));

        // ?????? ?????????????????????????????? ?????????????????? - ??????-???? ?????????????????? ???? ????????????????, ???? ?????????????????? 10
        mvc.perform(get("/api/user/tag/name?currPage=1").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(13)))
                .andExpect(jsonPath("$.items").value(hasSize(10)));

        // ???????????? ???? ?????????????? ??????-???? ???????????? ?????? ????????
        mvc.perform(get("/api/user/tag/name?currPage=2&items=30").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(30)))
                .andExpect(jsonPath("$.totalResultCount", is(13)))
                .andExpect(jsonPath("$.items").isEmpty());

        // ?????? ?????????????????????????? ?????????????????? - ?????????????? ????????????????
        mvc.perform(get("/api/user/tag/name?items=4").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    //???????? ?????????????????? ???? ????????
    @Test
    @DataSet(value = {TAG_PERSIST_DATE, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getTagsPaginationByDate() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // ?????????????????????? ????????????
        mvc.perform(get(GET_TAGS_BY_DATE + "?currPage=1&items=10").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(13)))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(109, 102, 110)));

        // ?????????????????????? ???????????? ???? ???????????? ????????????????
        mvc.perform(get(GET_TAGS_BY_DATE + "?currPage=2&items=10").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(13)))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(112, 111, 101)));

        // ?????? ?????????????????????????????? ?????????????????? - ??????-???? ?????????????????? ???? ????????????????, ???? ?????????????????? 10
        mvc.perform(get(GET_TAGS_BY_DATE + "?currPage=1").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(13)))
                .andExpect(jsonPath("$.items").value(hasSize(10)));

        // ???????????? ???? ?????????????? ??????-???? ???????????? ?????? ????????
        mvc.perform(get(GET_TAGS_BY_DATE + "?currPage=2&items=30").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(30)))
                .andExpect(jsonPath("$.totalResultCount", is(13)))
                .andExpect(jsonPath("$.items").isEmpty());

        // ?????? ?????????????????????????? ?????????????????? - ?????????????? ????????????????
        mvc.perform(get(GET_TAGS_BY_DATE + "?items=4").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }


    // ???????? ?????????????????? TagDto ?????????????????????????????? ???? ????????????????????????

    @Test
    @DataSet(value = {QUESTION_BY_POPULAR, TAG_ENTITY, QUESTION_HAS_TAG_BY_POPULAR, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void getTagsPaginationOrderByPopular() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // ?????????????????????? ????????????
        mvc.perform(get(GET_TAGS_BY_POPULAR + "?currPage=1&items=3").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(4)))
                .andExpect(jsonPath("$.itemsOnPage", is(3)))
                .andExpect(jsonPath("$.totalResultCount", is(12)))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(100, 111, 101)));

        // ?????? ?????????????????????????????? ?????????????????? - ??????-???? ?????????????????? ???? ????????????????, ???? ?????????????????? 10
        mvc.perform(get(GET_TAGS_BY_POPULAR + "?currPage=1").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(12)))
                .andExpect(jsonPath("$.items").value(hasSize(10)));

        // ???????????? ???? ?????????????? ??????-???? ???????????? ?????? ????????
        mvc.perform(get(GET_TAGS_BY_POPULAR + "?currPage=2&items=20").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(20)))
                .andExpect(jsonPath("$.totalResultCount", is(12)))
                .andExpect(jsonPath("$.items").isEmpty());

        // ?????? ?????????????????????????? ?????????????????? - ?????????????? ????????????????
        mvc.perform(get(GET_TAGS_BY_POPULAR + "?items=4").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DataSet(value = {TAG_ENTITY_ADD_TRACKED_OR_IGNORED_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    void addTagTrackedStatusOk() throws Exception {
        ResultActions response = mvc.perform(post(ADD_TRACKED_TAG).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("java")))
                .andExpect(jsonPath("$.description", nullValue()));
    }

    @Test
    @DataSet(value = {TAG_ENTITY_ADD_TRACKED_OR_IGNORED_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    void addTagIgnoredStatusOk() throws Exception {
        ResultActions response = mvc.perform(post(ADD_IGNORED_TAG).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("python")))
                .andExpect(jsonPath("$.description", nullValue()));
    }

    @Test
    @DataSet(value = {TAG_ENTITY_ADD_TRACKED_OR_IGNORED_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    void addTagTrackedStatusNotExistOnThisSite() throws Exception {
        ResultActions response = mvc.perform(post(ADD_TRACKED_TAG_NOT_EXIST).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isBadRequest())
                .andExpect(content().string("The tag does not exist on this site"));
    }

    @Test
    @DataSet(value = {TAG_ENTITY_ADD_TRACKED_OR_IGNORED_TAG, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    void addTagIgnoredStatusNotExistOnThisSite() throws Exception {
        ResultActions response = mvc.perform(post(ADD_IGNORED_TAG_NOT_EXIST).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isBadRequest())
                .andExpect(content().string("The tag does not exist on this site"));
    }
}