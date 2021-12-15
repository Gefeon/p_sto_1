package com.javamentor.qa.platform.webapp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("loginPage");
        registry.addViewController("/test").setViewName("testPage");
        registry.addViewController("/user").setViewName("userPage");
        registry.addViewController("/users").setViewName("usersPage");
        registry.addViewController("/question/ask").setViewName("askQuestion");
        registry.addViewController("/question/{id}").setViewName("questionPage");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
