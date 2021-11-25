package com.javamentor.qa.platform;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = JmApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionResourceControllerTests {

    private final TestRestTemplate restTemplate;
    private final TestDataInitService testDataInitService;
    private final String url = "/api/user/question";

    @Autowired
    public QuestionResourceControllerTests(TestRestTemplate restTemplate, TestDataInitService testDataInitService) {
        this.restTemplate = restTemplate;
        this.testDataInitService = testDataInitService;
    }

    @BeforeEach
    void dbInit() {
        testDataInitService.fillTableWithTestData();
    }

    @Test
    public void postCorrectDataCheckResponse() {
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

        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), greaterThan(0L));
        assertThat(response.getBody().getTitle(), is(questionCreateDto.getTitle()));
        assertThat(response.getBody().getDescription(), not(emptyOrNullString()));
        assertThat(response.getBody().getAuthorId(), greaterThan(0L));
        assertThat(response.getBody().getCountAnswer(), greaterThanOrEqualTo(0));
        assertThat(response.getBody().getViewCount(), greaterThanOrEqualTo(0));
        assertThat(response.getBody().getCountValuable(), notNullValue());
        assertThat(response.getBody().getPersistDateTime(), isA(LocalDateTime.class));
        assertThat(response.getBody().getLastUpdateDateTime(), isA(LocalDateTime.class));
        assertThat(response.getBody().getListTagDto(), iterableWithSize(randomSize));
        assertThat(response.getBody().getListTagDto().get(0).getId(), greaterThan(0L));
    }

    @Test
    public void postBlankTitleGetBadRequest() {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle(" ");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void postNullDescriptionGetBadRequest() {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void postNullTagsGetBadRequest() {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");

        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void postTagsWithNonexistentIdGet() {

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");
        TagDto tag1 = new TagDto();
        TagDto tag2 = new TagDto();
        tag1.setName("tagName1");
        tag2.setName("tagName2");
        questionCreateDto.setTags(List.of(tag1,tag2));

        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getListTagDto(), iterableWithSize(2));
        assertThat(response.getBody().getListTagDto().get(0).getId(), greaterThan(0L));
    }


}
