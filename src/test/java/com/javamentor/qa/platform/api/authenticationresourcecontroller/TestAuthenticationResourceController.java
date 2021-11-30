package com.javamentor.qa.platform.api.authenticationresourcecontroller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.jwt.JwtService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
@Getter @NoArgsConstructor
public class TestAuthenticationResourceController {
    private static final String AUTH_URI = "/api/auth/token";
    private static final String WITH_AUTH_URI = "/api/user/stub";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Autowired private TestDataInitService testDataInitService;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtService jwtService;
    @Autowired private UserService userService;
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @PostConstruct
    public void setup() {
        testDataInitService.fillTableWithTestData();
    }

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(WITH_AUTH_URI)).andExpect(status().isForbidden());
    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {
        User user = userService.getById(1L).orElseThrow();
        AuthenticationRequestDto authDto = new AuthenticationRequestDto(user.getUsername(), user.getPassword());

        mvc.perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void shouldGetAccessToSecuredResource() throws Exception {
        User user = userService.getById(1L).orElseThrow();
        TokenResponseDto token = jwtService.createAccessToken(user.getUsername(), "ROLE_USER");
        assertNotNull(token);

        MvcResult result = mvc.perform(get(WITH_AUTH_URI).header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isOk()).andReturn();

        DecodedJWT decodedJWT = jwtService.processToken(result.getRequest()).orElseThrow();
        assertEquals(decodedJWT.getExpiresAt().getTime() - decodedJWT.getIssuedAt().getTime(), jwtService.getAccessTokenValidTime());
    }

    @Test
    public void shouldNotAllowedAccessToUserWithExpiredToken() throws Exception {
        JwtService jwtServiceSpy = Mockito.spy(jwtService);
        Field field = JwtService.class.getDeclaredField("accessTokenValidTime");
        field.setAccessible(true);
        ReflectionUtils.setField(field, jwtServiceSpy, -1000L);
        assertEquals(-1000L, jwtServiceSpy.getAccessTokenValidTime());

        UserDetails userDetails = userDetailsService.loadUserByUsername(userService.getById(1L).orElseThrow().getUsername());
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElseThrow();
        TokenResponseDto token = jwtServiceSpy.createAccessToken(userDetails.getUsername(), role);
        assertNotNull(token);

        mvc.perform(get(WITH_AUTH_URI).header(AUTH_HEADER, PREFIX + token.getToken()))
                .andExpect(status().isBadRequest());
    }
}
