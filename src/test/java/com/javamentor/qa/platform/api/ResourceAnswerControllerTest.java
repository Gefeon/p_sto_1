package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
class ResourceAnswerControllerTest extends AbstractTestControllerClass {

    private final String url = "/api/user/question/100/answer/100";

    @Test
    @DataSet(value = "dataset/initialize/answerResourceController/correctId.yml")
    void deleteAnswer_returnStatusOk_AnswerDeleted() throws Exception {

        ResultActions response = mockMvc.perform(delete(url));
        response.andExpect(status().isOk());
    }

    @Test
    void deleteAnswerWithIncorrectId_returnBadRequest() throws Exception {

        ResultActions response = mockMvc.perform(delete(url));
        response.andExpect(status().isBadRequest());
    }
}