package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceAnswerControllerTest extends AbstractTestControllerClass{

    private final String url = "/api/user/question/100/answer/100";

    @Test
    @DataSet(value = "dataset/initialize/answerResourceController/incorrectId.yml")
    @ExpectedDataSet(value = "dataset/expected/answerResourceController/incorrectId.yml")
    public void deleteAnswerWithIncorrectId_returnBadRequest() throws Exception {
        ResultActions response = mockMvc.perform(delete(url));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = "dataset/initialize/answerResourceController/correctId.yml")
    @ExpectedDataSet(value = "dataset/expected/answerResourceController/correctId.yml")
    public void deleteAnswer_returnStatusOk_AnswerDeleted() throws Exception {
        ResultActions response = mockMvc.perform(delete(url));
        response.andExpect(status().isOk());
    }

}