package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestQuestionResourceController extends AbstractTestControllerClass {

    private final String url = "/api/user/question";

    @Test
    @DataSet(value = "dataset/initialize/questionResourceController/common.yml")
    public void postCorrectData_checkResponse() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("question title");
        List<TagDto> tags = new ArrayList<>();
        int randomSize = ThreadLocalRandom.current().nextInt(1, 20);
        for (int i = 0; i < randomSize; i++) {
            TagDto tag = new TagDto();
            tag.setName("tagName" + i);
            tags.add(tag);
        }
        questionCreateDto.setTags(tags);

        ResultActions response = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0L), Long.class))
                .andExpect(jsonPath("$.title").value(questionCreateDto.getTitle()))
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.authorId").value(greaterThan(0L), Long.class))
                .andExpect(jsonPath("$.countAnswer").value(greaterThanOrEqualTo(0L), Long.class))
                .andExpect(jsonPath("$.viewCount").value(greaterThanOrEqualTo(0L), Long.class))
                .andExpect(jsonPath("$.countValuable").isNotEmpty())
                .andExpect(jsonPath("$.persistDateTime").value(startsWith(LocalDate.now().toString())))
                .andExpect(jsonPath("$.lastUpdateDateTime").value(startsWith(LocalDate.now().toString())))
                .andExpect(jsonPath("$.listTagDto").value(iterableWithSize(randomSize)))
                .andExpect(jsonPath("$.listTagDto[0].id").value(greaterThan(0L), Long.class));
    }

    @Test
    @DataSet(value = "dataset/initialize/questionResourceController/common.yml")
    public void postBlankTitle_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle(" ");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        MvcResult result = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }


    @Test
    @DataSet(value = "dataset/initialize/questionResourceController/common.yml")
    public void postNullDescription_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        MvcResult result = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }

    @Test
    @DataSet(value = "dataset/initialize/questionResourceController/common.yml")
    public void postNullTags_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");

        MvcResult result = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));

    }

    @Test
    @DataSet(value = "dataset/initialize/questionResourceController/common.yml")
    public void postTagsWithNonexistentId_checkNewTagsAdded() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");
        TagDto tag1 = new TagDto();
        TagDto tag2 = new TagDto();
        tag1.setName("newTagName1");
        tag2.setName("newTagName2");
        questionCreateDto.setTags(List.of(tag1, tag2));

        int tagsCount = tagService.getAll().size();
        MvcResult result = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        QuestionDto questionDto = objectMapper.readValue(result.getResponse().getContentAsString(), QuestionDto.class);
        assertThat(tagService.getAll().size(), is(tagsCount + 2));
        assertThat(tagService.existsByName(tag1.getName()), is(true));
        assertThat(tagService.existsByName(tag2.getName()), is(true));
        assertThat(questionService.getWithTagsById(questionDto.getId()).orElseThrow()
                .getTags().stream().filter(tag -> tag.getName().equals(tag1.getName()))
                .findFirst().orElse(null), notNullValue());
    }

    @Test
    @DataSet(value = "dataset/initialize/questionResourceController/common.yml")
    public void postTagsWithExistentId_checkNewTagsAdded() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");
        TagDto tag1 = new TagDto();
        TagDto tag2 = new TagDto();
        tag1.setName("newTagName1");
        tagService.persist(tagMapper.toModel(tag1));
        tag2.setName("newTagName2");
        questionCreateDto.setTags(List.of(tag1, tag2));

        int tagsCount = tagService.getAll().size();
        MvcResult result = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        QuestionDto questionDto = objectMapper.readValue(result.getResponse().getContentAsString(), QuestionDto.class);
        assertThat(tagService.getAll().size(), is(tagsCount + 1));
        assertThat(tagService.existsByName(tag1.getName()), is(true));
        assertThat(tagService.existsByName(tag2.getName()), is(true));
        assertThat(questionService.getWithTagsById(questionDto.getId()).orElseThrow()
                .getTags().stream().filter(tag -> tag.getName().equals(tag1.getName()))
                .findFirst().orElse(null), notNullValue());
    }

    @Test
    @DataSet(value = "dataset/initialize/questionResourceController/common.yml")
    public void postQuestion_checkIsAdded() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        int questionsCount = questionService.getAll().size();
        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertThat(questionService.getAll().size(), is(questionsCount + 1));
    }
}
