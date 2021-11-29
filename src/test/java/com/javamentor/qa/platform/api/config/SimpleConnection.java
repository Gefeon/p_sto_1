package com.javamentor.qa.platform.api.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SimpleConnection {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
                        "jm?characterEncoding=UTF-8&useUnicode=true" +
                        "&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    "postgres", "root");
    }
}
