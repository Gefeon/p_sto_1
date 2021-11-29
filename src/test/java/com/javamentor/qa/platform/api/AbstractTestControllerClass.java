package com.javamentor.qa.platform.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.mapper.TagMapper;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public abstract class AbstractTestControllerClass {
    protected final TagService tagService;
    protected final QuestionService questionService;
    protected final MockMvc mockMvc;
    protected final TagMapper tagMapper;
    protected final ObjectMapper objectMapper;

    public AbstractTestControllerClass(TagService tagService,
                                          QuestionService questionService, MockMvc mockMvc, TagMapper tagMapper, ObjectMapper objectMapper) {
        this.tagService = tagService;
        this.questionService = questionService;
        this.mockMvc = mockMvc;
        this.tagMapper = tagMapper;
        this.objectMapper = objectMapper;
    }

    @AfterEach
    public void end(){

    }

}
