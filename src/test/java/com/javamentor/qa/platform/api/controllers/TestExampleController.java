package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;

import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = {"spring.config.location = src/test/resources/application-test.properties"})
@DBUnit(caseSensitiveTableNames = true)
@AutoConfigureMockMvc
public class TestExampleController {

    @Autowired
    private MockMvc mvc;

    @Test
    @DataSet(value = "dataset/DBRoleExample.yml", disableConstraints = true)
    public void shouldGetListOfRoles() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/listRoles").accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].name", is("ROLE_ADMIN")))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].name", is("ROLE_USER")));
    }

    @Test
    @DataSet(value= "dataset/expected/DBUserExample.yml")
    public void shouldGetListOfUsers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/listUsers").accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].email", is("admin100@admin.ru")))
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].email", is("user101@user.ru")));
    }
}


