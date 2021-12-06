package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import com.javamentor.qa.platform.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuthenticationResourceController extends AbstractTestApi {
    private static final String USER_ENTITY = "authenticationresourcecontroller/user_entity.yml";
    private static final String ROLE_ENTITY = "authenticationresourcecontroller/role.yml";

    private static final String AUTH_URI = "/api/auth/token";
    private static final String WITH_AUTH_URI = "/api/user/stub";
    private static final String WITH_NO_AUTH_URI = "/login";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    public void shouldNotAllowAccessToPrivateResourceToUnauthenticatedRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(WITH_AUTH_URI)).andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAccessToPublicResourceToUnauthenticatedRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(WITH_NO_AUTH_URI)).andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void shouldAllowAccessToPrivateResourceToAuthorizedRequest() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        mvc.perform(get(WITH_AUTH_URI).header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void shouldNotAllowAccessToPrivateResourceIfTokenIsExpired() throws Exception {
        JwtService jwtServiceSpy = Mockito.spy(jwtService);
        Field field = JwtService.class.getDeclaredField("accessTokenValidTime");
        field.setAccessible(true);
        ReflectionUtils.setField(field, jwtServiceSpy, -1000L);
        assertEquals(-1000L, jwtServiceSpy.getAccessTokenValidTime());

        TokenResponseDto token = jwtServiceSpy.createAccessToken("user100@user.ru", "ROLE_USER");
        assertNotNull(token);

        mvc.perform(get(WITH_AUTH_URI).header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isForbidden());
    }
}
