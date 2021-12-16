package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestUserResourceController extends AbstractTestApi {

    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private static final String USER_ENTITY = "datasets/userresourcecontroller/UserDto.yml";
    private static final String ROLE_REP_ENTITY = "datasets/userresourcecontroller/RoleReputation.yml";
    private static final String REPUTATION_ENTITY = "datasets/userresourcecontroller/Reputation.yml";
    private static final String QUESTION_ENTITY = "datasets/userresourcecontroller/Question.yml";
    private static final String ANSWER_ENTITY = "datasets/userresourcecontroller/Answer.yml";
    private static final String USER_BY_PERSIST_DATE = "datasets/userresourcecontroller/paginationByPersistDate/user_entity.yml";
    private static final String ROLE_ENTITY = "datasets/userresourcecontroller/Role.yml";
    private static final String REPUTATION_BY_PERSIST_DATE = "datasets/userresourcecontroller/paginationByPersistDate/reputation.yml";

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_REP_ENTITY, REPUTATION_ENTITY, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoById() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        //user exist
        mvc.perform(get("/api/user/103").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
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

    /*
     * Тест пагинации userDto по reputation
     * */
    @Test
    @DataSet(value = {USER_ENTITY, ROLE_REP_ENTITY, REPUTATION_ENTITY, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getReputation() throws Exception {

        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        // стандартный запрос
        mvc.perform(get("/api/user/reputation?currPage=2&items=3").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(3)))
                .andExpect(jsonPath("$.totalResultCount", is(4)))
                .andExpect(jsonPath("$.items").isNotEmpty());

        // запрос на большее кол-во данных чем есть
        mvc.perform(get("/api/user/reputation?currPage=2&items=300").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(300)))
                .andExpect(jsonPath("$.totalResultCount", is(4)))
                .andExpect(jsonPath("$.items").isEmpty());

        // нет обязательного параметра - текущей страницы
        mvc.perform(get("/api/user/reputation?items=4").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());

        // текущая страница велика
        mvc.perform(get("/api/user/reputation?currPage=3&items=3").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(3)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(3)))
                .andExpect(jsonPath("$.totalResultCount", is(4)))
                .andExpect(jsonPath("$.items").isEmpty());

        // нет необязательного параметра - кол-во элементов на странице, по умолчанию 10
        mvc.perform(get("/api/user/reputation?currPage=1").header(AUTH_HEADER, PREFIX + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(4)))
                .andExpect(jsonPath("$.items[*].reputation").value(containsInRelativeOrder(41, 22, 11, 10)));
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDate_expectCorrectData() throws Exception {

        ResultActions response = mvc.perform(get("/api/user/new?currPage=2&items=5").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andDo(print())
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


        // нет обязательного параметра - текущей страницы
        ResultActions response = mvc.perform(get("/api/user/new?items=4").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateWithoutNonRequiredParam_expectTenElementsOnPage() throws Exception {
        // нет необязательного параметра - кол-во элементов на странице по умолчанию 10
        ResultActions response = mvc.perform(get("/api/user/new?currPage=1").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andDo(print())
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

        // страница больше, чем элементов
        ResultActions response = mvc.perform(get("/api/user/new?currPage=2&items=100").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateZeroRequiredParam_expectBadRequest() throws Exception {
        // параметр страницы равен 0
        ResultActions response = mvc.perform(get("/api/user/new?currPage=0&items=100").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateZeroNonRequiredParam_expectBadRequest() throws Exception {
        // параметр элементов на странице равен 0
        ResultActions response = mvc.perform(get("/api/user/new?currPage=10&items=0").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {USER_BY_PERSIST_DATE, ROLE_ENTITY, REPUTATION_BY_PERSIST_DATE, QUESTION_ENTITY, ANSWER_ENTITY}, disableConstraints = true)
    public void getUserDtoByPersistDateAllItemsPerOnePage_expectAllItemsFromDB() throws Exception {
        // хотим получить больше чем есть
        ResultActions response = mvc.perform(get("/api/user/new?currPage=1&items=100").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(100)))
                .andExpect(jsonPath("$.totalResultCount", is(14)))
                .andExpect(jsonPath("$.items").value(hasSize(14)));
    }

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void changePassword() throws Exception {
        final String url = "/api/user/changePassword";
        final String CORRECT_PASS = "12345q!@#$%";
        final String TOO_SHORT_PASS = "12345";
        final String TOO_LONG_PASS = "12345qwertyui";
        final String BLANC_PASS = "      ";
        final String WRONG_CHARSET_PASS = "12345вася";
        String authHeader = PREFIX + getToken("user100@user.ru", "user");

        ResultActions response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).content(CORRECT_PASS));
        response.andExpect(status().isOk());

        authHeader = PREFIX + getToken("user100@user.ru", CORRECT_PASS);

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).content(TOO_SHORT_PASS));
        response.andExpect(status().isBadRequest()).andExpect(content()
                .string("Length of password from 6 to 12 symbols"));

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).content(TOO_LONG_PASS));
        response.andExpect(status().isBadRequest()).andExpect(content()
                .string("Length of password from 6 to 12 symbols"));

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).content(BLANC_PASS));
        response.andExpect(status().isBadRequest()).andExpect(content()
                .string("changePassword.password: Password cannot be empty"));

        response = mvc.perform(put(url).header(AUTH_HEADER, authHeader).content(WRONG_CHARSET_PASS));
        response.andExpect(status().isBadRequest()).andExpect(content()
                .string("Use only latin alphabet, numbers and special chars"));
    }

}