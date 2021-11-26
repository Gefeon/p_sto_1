package com.javamentor.qa.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.question.AnswerService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
class ResourceAnswerControllerTest {

    @MockBean
    private AnswerService answerService;

    private final MockMvc mockMvc;
    private final String url = "/api/user/question/1/answer/1";
    private final ObjectMapper objectMapper;

    @Autowired
    public ResourceAnswerControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void deleteAnswer_returnStatusOk_AnswerDeleted() throws Exception {
        Answer answer = new Answer();
        Mockito.doReturn(Optional.of(answer)).when(answerService).getById(Mockito.anyLong());
        ResultActions response = mockMvc.perform(delete(url));
        response.andExpect(status().isOk());
        Mockito.verify(answerService).delete(answer);
    }

    @Test
    void deleteAnswerWithIncorrectId_returnBadRequest() throws Exception {
        Answer answer = new Answer();
        Mockito.doReturn(Optional.empty()).when(answerService).getById(Mockito.anyLong());
        ResultActions response = mockMvc.perform(delete(url));
        response.andExpect(status().isBadRequest());
        Mockito.verify(answerService, Mockito.never()).delete(answer);
    }
}