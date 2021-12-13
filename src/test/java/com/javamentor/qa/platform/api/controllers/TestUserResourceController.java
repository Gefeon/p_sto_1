package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUserResourceController extends AbstractTestApi {

    private static final String USER_ENTITY = "datasets/userresourcecontroller/UserDto.yml";
    private static final String USER_BY_PERSIST_DATE = "datasets/userresourcecontroller/paginationByPersistDate/user_entity.yml";
    private static final String ROLE_ENTITY = "datasets/userresourcecontroller/Role.yml";
    private static final String REPUTATION_ENTITY = "datasets/userresourcecontroller/Reputation.yml";
    private static final String REPUTATION_BY_PERSIST_DATE = "datasets/userresourcecontroller/paginationByPersistDate/reputation.yml";
    private static final String QUESTION_ENTITY = "datasets/userresourcecontroller/Question.yml";
    private static final String ANSWER_ENTITY = "datasets/userresourcecontroller/Answer.yml";

    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY, REPUTATION_ENTITY, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoById() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        //user exist
        mvc.perform(get("/api/user/103").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.id", is(103)))
                .andExpect(jsonPath("$.fullName", is("Roman")))
                .andExpect(jsonPath("$.email", is("Rom@ya.ru")))
                .andExpect(jsonPath("$.city", is("Surgut")))
                .andExpect(jsonPath("$.linkImage", nullValue()))
                .andExpect(jsonPath("$.reputation", is(41)));

        //id is absent
        mvc.perform(get("/api/user/").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.fullName").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.city").doesNotExist())
                .andExpect(jsonPath("$.linkImage").doesNotExist())
                .andExpect(jsonPath("$.reputation").doesNotExist())
                .andExpect(jsonPath("$").doesNotExist());

        //user is absent
        mvc.perform(get("/api/user/1000").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.fullName").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.city").doesNotExist())
                .andExpect(jsonPath("$.linkImage").doesNotExist())
                .andExpect(jsonPath("$.reputation").doesNotExist())
                .andExpect(jsonPath("$").value("User is absent or wrong Id"));

        //wrong type id
        mvc.perform(get("/api/user/ggg").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.fullName").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.city").doesNotExist())
                .andExpect(jsonPath("$.linkImage").doesNotExist())
                .andExpect(jsonPath("$.reputation").doesNotExist())
                .andExpect(jsonPath("$").doesNotExist());
    }


    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDate_expectCorrectData() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // стандартный запрос
        mvc.perform(get("/api/user/new?currPage=2&items=5").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(3)))
                .andExpect(jsonPath("$.itemsOnPage", is(5)))
                .andExpect(jsonPath("$.totalResultCount", is(14)))
                .andExpect(jsonPath("$.items").value(hasSize(5)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(104, 105, 106, 108, 109)));
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateWithoutRequiredParam_expectBadRequest() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // нет обязательного параметра - текущей страницы
        mvc.perform(get("/api/user/new?items=4").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateWithoutNonRequiredParam_expectTenElementsOnPage() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // нет необязательного параметра - кол-во элементов на странице по умолчанию 10
        mvc.perform(get("/api/user/new?currPage=1").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(14)))
                .andExpect(jsonPath("$.items").value(hasSize(10)));
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateWithPageNumberBiggerThanHaveItems_expectEmptyItemsArray() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // страница больше, чем элементов
        mvc.perform(get("/api/user/new?currPage=2&items=100").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateZeroRequiredParam_expectBadRequest() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // параметр страницы равен 0
        mvc.perform(get("/api/user/new?currPage=0&items=100").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateZeroNonRequiredParam_expectBadRequest() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // параметр элементов на странице равен 0
        mvc.perform(get("/api/user/new?currPage=10&items=0").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateAllItemsPerOnePage_expectAllItemsFromDB() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // хотим получить больше чем есть
        mvc.perform(get("/api/user/new?currPage=1&items=100").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(100)))
                .andExpect(jsonPath("$.totalResultCount", is(14)))
                .andExpect(jsonPath("$.items").value(hasSize(14)));
    }

}
