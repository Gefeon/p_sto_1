package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAdminResourceController extends AbstractTestApi {
    private static final String USER_ENTITY = "adminresourcecontroller/user_entity.yml";
    private static final String ROLE_ENTITY = "adminresourcecontroller/role.yml";

    private static final String AUTH_URI = "/api/auth/token";
    private static final String DELETE_URI = "/api/admin/delete/";
    private static final String WITH_AUTH_URI = "/api/user/stub";

    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void shouldDeleteUser() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("admin100@admin.ru", "admin");
        String token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class).getToken();

        mvc.perform(post(DELETE_URI + "/user101@user.ru").header(AUTH_HEADER, PREFIX + token))
                .andExpect(status().isNoContent());

        mvc.perform(post(DELETE_URI + "/test@test.ru").header(AUTH_HEADER, PREFIX + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void shouldNotGetAccessToDeletedUser() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user102deleted@user.ru", "user");

        mvc.perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void shouldNotGetAccessToDeletedUserWithValidJwt() throws Exception {
        AuthenticationRequestDto authDtoAdmin = new AuthenticationRequestDto("admin100@admin.ru", "admin");
        String tokenAdmin = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDtoAdmin)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class).getToken();

        AuthenticationRequestDto authDtoUser = new AuthenticationRequestDto("user101@user.ru", "user");
        String tokenUser = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDtoUser)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class).getToken();

        //before delete
        mvc.perform(get(WITH_AUTH_URI).header(AUTH_HEADER, PREFIX + tokenUser)).andExpect(status().isOk());
        //deleting
        mvc.perform(post(DELETE_URI + "/user101@user.ru").header(AUTH_HEADER, PREFIX + tokenAdmin));
        //after delete
        mvc.perform(get(WITH_AUTH_URI).header(AUTH_HEADER, PREFIX + tokenUser)).andExpect(status().isForbidden());
    }
}
