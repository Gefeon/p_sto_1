package com.javamentor.qa.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.mapper.TagMapper;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class QuestionResourceControllerTests {

    private MockMvc mockMvc;
    private  final TestDataInitService testDataInitService;
    private final TagService tagService;
    private final QuestionService questionService;
    private final TagMapper tagMapper;
    private final String url = "/api/user/question";
    private final ObjectMapper objectMapper;

    @Autowired
    public QuestionResourceControllerTests(TestDataInitService testDataInitService, TestDataInitService testDataInitService1, TagService tagService,
                                           QuestionService questionService, TagMapper tagMapper, ObjectMapper objectMapper) {
        this.testDataInitService = testDataInitService1;
        this.tagService = tagService;
        this.questionService = questionService;
        this.tagMapper = tagMapper;
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setup() {
        testDataInitService.fillTableWithTestData();
    }

    @AfterEach
    public void clearTags() {
       testDataInitService.clearTestData();
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
    }



    @Test
    public void postBlankTitle_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle(" ");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    public void postNullDescription_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    public void postNullTags_getBadRequest() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");

        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
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
                .andExpect(status().isOk()).andReturn();

        QuestionDto questionDto = objectMapper.readValue(result.getResponse().getContentAsString(), QuestionDto.class);

        assertThat(tagService.getAll().size(), is(tagsCount + 2));
        assertThat(tagService.existsByName(tag1.getName()), is(true));
        assertThat(tagService.existsByName(tag2.getName()), is(true));
        assertThat(questionService.getWithTagsById(questionDto.getId()).orElseThrow()
                .getTags().stream().filter(tag -> tag.getName().equals(tag1.getName()))
                .findFirst().orElse(null), notNullValue());
    }

    @Test
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
                .andExpect(status().isOk()).andReturn();

        QuestionDto questionDto = objectMapper.readValue(result.getResponse().getContentAsString(), QuestionDto.class);

        assertThat(tagService.getAll().size(), is(tagsCount + 1));
        assertThat(tagService.existsByName(tag1.getName()), is(true));
        assertThat(tagService.existsByName(tag2.getName()), is(true));
        assertThat(questionService.getWithTagsById(questionDto.getId()).orElseThrow()
                .getTags().stream().filter(tag -> tag.getName().equals(tag1.getName()))
                .findFirst().orElse(null), notNullValue());
    }

    @Test
    public void postQuestion_checkIsAdded() throws Exception {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        int questionsCount = questionService.getAll().size();
        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(questionCreateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(questionService.getAll().size(), is(questionsCount + 1));

    }
}
