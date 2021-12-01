package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestQuestionResourceController extends AbstractTestControllerClass {

    private final String url = "/api/user/question";

    @Test
    @DataSet(value = "dataset/common.yml")
    @ExpectedDataSet(value = "dataset/expected/common.yml")
    public void postCorrectData_checkResponse() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("question title");
        List<TagDto> tags = new ArrayList<>();
        int tagCount = 3;
        for (int i = 0; i < tagCount; i++) {
            TagDto tag = new TagDto();
            tag.setName("tagName" + i);
            tags.add(tag);
        }
        questionCreateDto.setTags(tags);

        ResultActions response = mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(questionCreateDto))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0L), Long.class))
                .andExpect(jsonPath("$.title").value(questionCreateDto.getTitle()))
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.authorId").value(greaterThan(0L), Long.class))
                .andExpect(jsonPath("$.countAnswer").value(0))
                .andExpect(jsonPath("$.viewCount").value(0))
                .andExpect(jsonPath("$.countValuable").value(0))
                .andExpect(jsonPath("$.listTagDto").value(iterableWithSize(tagCount)))
                .andExpect(jsonPath("$.listTagDto[0].id").value(greaterThan(0L), Long.class));
    }

    @Test
    @DataSet(value = "dataset/common.yml")
    public void postBlankTitle_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle(" ");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        MvcResult result = mockMvc
                .perform(post(url)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }

    @Test
    @DataSet(value = "dataset/common.yml")
    public void postNullDescription_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        MvcResult result = mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }

    @Test
    @DataSet(value = "dataset/common.yml")
    public void postNullTags_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");

        MvcResult result = mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));

    }

    @Test
    @DataSet(value = "dataset/common.yml")
    @ExpectedDataSet(value = "dataset/expected/newUniqueIdAdded.yml")
    public void postTagsWithUniqueId_checkNewTagsAdded() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("question title");
        TagDto tag1 = new TagDto();
        TagDto tag2 = new TagDto();
        tag1.setName("tagName0");
        tag2.setName("tagName1");
        questionCreateDto.setTags(List.of(tag1, tag2));

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    @DataSet(value = "dataset/common.yml")
    @ExpectedDataSet(value = "dataset/expected/UniqueAndExistentIdAdded.yml")
    public void postTagsWithExistentId_checkNewTagsAdded() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("question title");
        TagDto tag1 = new TagDto();
        TagDto tag2 = new TagDto();
        tag1.setName("db_architecture"); //existed
        tag2.setName("tagName1");
        questionCreateDto.setTags(List.of(tag1, tag2));

        MvcResult result = mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

    }

    @Test
    @DataSet(value = "dataset/common.yml")
    public void postQuestion_checkIsAdded() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
