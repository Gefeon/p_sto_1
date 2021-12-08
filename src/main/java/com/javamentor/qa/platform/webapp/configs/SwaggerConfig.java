package com.javamentor.qa.platform.webapp.configs;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Api(tags = {SwaggerConfig.QUESTION_CONTROLLER})
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String USER_CONTROLLER = "User";
    public static final String QUESTION_CONTROLLER = "Question";
    public static final String AUTHENTICATION_CONTROLLER = "Authentication";
    public static final String ADMIN_RESOURCE_CONTROLLER = "Admin";
    public static final String ANSWER_CONTROLLER = "Answer";
    public static final String TAG_CONTROLLER = "Tag";
    public static final String RESOURCE_TAG_CONTROLLER = "Related tags";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .tags(
                        new Tag(USER_CONTROLLER,"These endpoints are used to manage the user details",1),
                        new Tag(AUTHENTICATION_CONTROLLER, "These endpoints are used to authenticate the client", 1),
                        new Tag(ADMIN_RESOURCE_CONTROLLER, "These endpoints are used to admin actions", 1),
                        new Tag(ANSWER_CONTROLLER,"These endpoints are used to manage the user answers",1),
                        new Tag(RESOURCE_TAG_CONTROLLER, "This endpoint is used to manage related tags"))
                        new Tag(ANSWER_CONTROLLER,"These endpoints are used to manage the user answers",1),
                        new Tag(TAG_CONTROLLER,"These endpoints are used to manage the user tags",1))
                .apiInfo(apiInfo());
    }

    public ApiInfo apiInfo() {
        return new ApiInfo(
                "JM Stack REST API",
                "JM Stack Swagger REST Open API.",
                "v1.0.0",
                "Terms of service",
                new Contact("JM", "www.example.com", "myeaddress@company.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
