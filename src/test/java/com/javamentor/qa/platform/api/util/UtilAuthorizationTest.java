package com.javamentor.qa.platform.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UtilAuthorizationTest {

    private UtilAuthorizationTest(){}

    @Autowired private static ObjectMapper objectMapper;
    @Autowired private static MockMvc mvc;
    private static final String AUTH_URI = "/api/auth/token";

    public static String getToken(String username, String password) throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto(username, password);

        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        return token.getToken();
    }
}
