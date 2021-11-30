package com.javamentor.qa.platform.api;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.javamentor.qa.platform.api.config.SimpleConnection;
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
    protected MockMvc mockMvc;

    private static Connection conn;

    @BeforeClass
    public static void setupConnection() throws SQLException {
        conn = SimpleConnection.getConnection();
    }

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(conn);
}
