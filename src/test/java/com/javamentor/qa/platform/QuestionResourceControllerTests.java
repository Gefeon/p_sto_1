package com.javamentor.qa.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.mapper.TagMapper;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import com.mysql.cj.xdevapi.JsonNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class QuestionResourceControllerTests {

    private MockMvc mockMvc;

    private final TestDataInitService testDataInitService;

    private final TagService tagService;

    private final QuestionService questionService;
    private final TagMapper tagMapper;
    private final String url = "/api/user/question";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    public QuestionResourceControllerTests( TestDataInitService testDataInitService, TagService tagService, QuestionService questionService, TagMapper tagMapper) {
        this.testDataInitService = testDataInitService;
        this.tagService = tagService;
        this.questionService = questionService;
        this.tagMapper = tagMapper;
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        testDataInitService.fillTableWithTestData();
    }

    @Test
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
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0L),Long.class))
                .andExpect(jsonPath("$.title").value(questionCreateDto.getTitle()))
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.authorId").value(greaterThan(0L),Long.class))
                .andExpect(jsonPath("$.countAnswer").value(greaterThanOrEqualTo(0L),Long.class))
                .andExpect(jsonPath("$.viewCount").value(greaterThanOrEqualTo(0L),Long.class))
                .andExpect(jsonPath("$.countValuable").isNotEmpty())
                .andExpect(jsonPath("$.persistDateTime").value(startsWith(LocalDate.now().toString())))
                .andExpect(jsonPath("$.lastUpdateDateTime").value(startsWith(LocalDate.now().toString())))
                .andExpect(jsonPath("$.listTagDto").value(iterableWithSize(randomSize)))
                .andExpect(jsonPath("$.listTagDto[0].id").value(greaterThan(0L),Long.class));


//        Mockito.when(questionService.persist()).thenReturn();
    }
//    }
//
//    @Test
//    public void postBlankTitleGetBadRequest() {
//
//        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
//        questionCreateDto.setDescription("question description");
//        questionCreateDto.setTitle(" ");
//        TagDto tag = new TagDto();
//        tag.setName("tagName");
//        questionCreateDto.setTags(List.of(tag));
//
//        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
//    }
//
//    @Test
//    public void postNullDescriptionGetBadRequest() {
//
//        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
//        questionCreateDto.setTitle("title");
//        TagDto tag = new TagDto();
//        tag.setName("tagName");
//        questionCreateDto.setTags(List.of(tag));
//
//        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
//    }
//
//    @Test
//    public void postNullTagsGetBadRequest() {
//
//        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
//        questionCreateDto.setDescription("question description");
//        questionCreateDto.setTitle("title");
//
//        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
//    }
//
//    @Test
//    public void postTagsWithNonexistentIdCheckNewTagsAdded() {
//
//        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
//        questionCreateDto.setDescription("question description");
//        questionCreateDto.setTitle("title");
//        TagDto tag1 = new TagDto();
//        TagDto tag2 = new TagDto();
//        tag1.setName("newTagName1");
//        tag2.setName("newTagName2");
//        questionCreateDto.setTags(List.of(tag1, tag2));
//
//        int tagsCount = tagService.getAll().size();
//        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//        assertThat(response.getBody().getListTagDto(), iterableWithSize(2));
//        assertThat(tagService.getAll().size(), is(tagsCount + 2));
//        assertThat(tagService.existsByName(tag1.getName()), is(true));
//        assertThat(tagService.existsByName(tag2.getName()), is(true));
//        assertThat(questionService.getWithTagsById(response.getBody().getId()).orElseThrow()
//                .getTags().stream().filter(tag -> tag.getName().equals(tag1.getName()))
//                .findFirst().orElse(null), notNullValue());
//    }
//
//    @Test
//    public void postTagsWithExistentIdCheckNewTagsAdded() {
//
//        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
//        questionCreateDto.setDescription("question description");
//        questionCreateDto.setTitle("title");
//        TagDto tag1 = new TagDto();
//        TagDto tag2 = new TagDto();
//        tag1.setName("newTagName1");
//        tagService.persist(tagMapper.toModel(tag1));
//        tag2.setName("newTagName2");
//        questionCreateDto.setTags(List.of(tag1, tag2));
//
//        int tagsCount = tagService.getAll().size();
//        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);
//
//        List<Tag> tagList = questionService.getWithTagsById(response.getBody().getId()).orElseThrow().getTags();
//
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//        assertThat(response.getBody().getListTagDto(), iterableWithSize(2));
//        assertThat(tagService.getAll().size(), is(tagsCount + 1));
//        assertThat(tagService.existsByName(tag1.getName()), is(true));
//        assertThat(tagService.existsByName(tag2.getName()), is(true));
//        assertThat((tagList.stream().filter(tag -> tag.getName().equals(tag1.getName()))
//                .findFirst().orElse(null)), notNullValue());
//    }
//
//    @Test
//    public void postQuestionCheckIsAdded() {
//
//        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
//        questionCreateDto.setDescription("question description");
//        questionCreateDto.setTitle("title");
//        TagDto tag = new TagDto();
//        tag.setName("tagName");
//        questionCreateDto.setTags(List.of(tag));
//
//        int questionsCount = questionService.getAll().size();
//        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//        assertThat(questionService.getAll().size(), is(questionsCount + 1));
//
//    }
}
