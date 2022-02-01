package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.service.util.InviteService;
import com.javamentor.qa.platform.service.util.MailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestInviteController extends AbstractTestApi {

    private static final String USER_ENTITY = "dataset/inviteController/user_entity.yml";
    private static final String ROLE = "dataset/inviteController/role.yml";
    private static final String INVITE_URL = "/api/invite/";
    private static final String TEST_URL = "/api/user/";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @SpyBean private InviteService inviteService;
    @MockBean private MailService mailService;

    @Test
    @DataSet(value = {USER_ENTITY, ROLE}, disableConstraints = true)
    @ExpectedDataSet(value = {"dataset/expected/inviteController/user_entity.yml", ROLE})
    public void shouldInviteUser() throws Exception {
        Mockito.doNothing().when(mailService).send(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.when(inviteService.generatePassword()).thenReturn("generated_password");
        //invite user
        mvc.perform(post(INVITE_URL + "user101@user.ru").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isOk());
        cacheManager.getCache("user-email").clear();
        //invited user
        mvc.perform(get(TEST_URL).header(AUTH_HEADER, PREFIX + getToken("user101@user.ru", "generated_password")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {USER_ENTITY, ROLE}, disableConstraints = true)
    @ExpectedDataSet(value = {USER_ENTITY, ROLE})
    public void shouldNotInviteAnAlreadyExistUser() throws Exception {
        Mockito.doNothing().when(mailService).send(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        mvc.perform(post(INVITE_URL + "user100@user.ru").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isBadRequest());
    }
}
