package com.javamentor.qa.platform;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
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

    @Autowired
    public QuestionResourceControllerTests(TestRestTemplate restTemplate, TestDataInitService testDataInitService) {
        this.restTemplate = restTemplate;
        this.testDataInitService = testDataInitService;
    }

    @Test
    public void testGetUserById() {
        testDataInitService.fillTableWithTestData();
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("sd");
        questionCreateDto.setTitle("question title");
        List<TagDto> tags = new ArrayList<>();
        int randomSize = ThreadLocalRandom.current().nextInt(0,20);
        for(int i = 0; i < randomSize; i++){
            TagDto tag = new TagDto();
            tag.setName("tagName" + i);
            tags.add(tag);
        }
        questionCreateDto.setTags(tags);
        String url = "/api/user/question";
        ResponseEntity<QuestionDto> response = restTemplate.postForEntity(url, questionCreateDto, QuestionDto.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), greaterThan(0L));
        assertThat(response.getBody().getTitle(), is("question title"));
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


}
