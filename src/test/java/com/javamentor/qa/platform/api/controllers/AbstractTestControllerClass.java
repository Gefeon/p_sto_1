package com.javamentor.qa.platform.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@DBRider
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true)
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource("classpath:application-test.properties")
public abstract class AbstractTestControllerClass {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
