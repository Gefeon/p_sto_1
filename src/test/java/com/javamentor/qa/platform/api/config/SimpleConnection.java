package com.javamentor.qa.platform.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class SimpleConnection {
@Bean
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
                        "jm?characterEncoding=UTF-8&useUnicode=true" +
                        "&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                "postgres", "root");
    }
}