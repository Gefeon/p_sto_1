package com.javamentor.qa.platform.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.javamentor.qa.platform.api.config.SimpleConnection;
import com.javamentor.qa.platform.models.mapper.TagMapper;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true)
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource("classpath:application-test.properties")
public abstract class AbstractTestControllerClass {
    @Autowired
    protected  TagService tagService;
    @Autowired
    protected  QuestionService questionService;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected  TagMapper tagMapper;
    @Autowired
    protected ObjectMapper objectMapper;

    private static Connection conn;

    @BeforeClass
    public static void setupConnection() throws SQLException {
        conn = SimpleConnection.getConnection();
    }

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(conn);
}
