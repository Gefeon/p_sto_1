package com.javamentor.qa.platform.webapp.configs;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("local")
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        String dbName = "";
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/" + dbName + "?characterEncoding=UTF-8&useUnicode=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("root");
        return dataSourceBuilder.build();
    }
}
