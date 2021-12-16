package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestQuestionResourceController extends AbstractTestApi {

    private final String url = "/api/user/question";
    private final String urlUpVote = "/api/user/question/100/upVote";
    private final String urlDownVote = "/api/user/question/100/downVote";


    private static final String USER_ENTITY = "dataset/QuestionResourceController/user.yml";
    private static final String ROLE_ENTITY = "dataset/QuestionResourceController/role.yml";
    private static final String QUESTION_ENTITY = "dataset/QuestionResourceController/question.yml";
    private static final String TAG_ENTITY = "dataset/QuestionResourceController/tag.yml";
    private static final String QUESTION_HAS_TAG_ENTITY = "dataset/QuestionResourceController/questionHasTag.yml";
    private static final String USER_ADD = "dataset/QuestionResourceController/UserAdd.yml";
    private static final String QUESTION_ADD = "dataset/QuestionResourceController/QuestionAdd.yml";

    private static final String NEW_QUESTION_ADDED = "dataset/expected/resourceQuestionController/newQuestionAdded.yml";
    private static final String THREE_TAGS_ADDED = "dataset/expected/resourceQuestionController/threeTagsAdded.yml";
    private static final String THREE_TAG_QUESTION_LINKS_ADDED = "dataset/expected/resourceQuestionController/threeQuestionHasTagsAdded.yml";
    private static final String TWO_UNIQUE_TAGS_ADDED = "dataset/expected/resourceQuestionController/twoUniqueIdTagsAdded.yml";
    private static final String TWO_UNIQUE_TAG_QUESTION_LINKS_ADDED = "dataset/expected/resourceQuestionController/twoQuestionHasTagsAdded.yml";
    private static final String TWO_EXISTENT_TAGS_ADDED = "dataset/expected/resourceQuestionController/twoExistentIdTagsAdded.yml";
    private static final String TWO_EXISTENT_TAG_QUESTION_LINKS_ADDED = "dataset/expected/resourceQuestionController/twoExistentIdQuestionHasTagAdded.yml";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {NEW_QUESTION_ADDED, THREE_TAGS_ADDED, THREE_TAG_QUESTION_LINKS_ADDED, USER_ENTITY, ROLE_ENTITY})
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

        ResultActions response = mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                .content(objectMapper.writeValueAsString(questionCreateDto))
                .contentType(MediaType.APPLICATION_JSON));
        response.andDo(print())
                .andExpect(status().isOk())
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
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void postBlankTitle_getBadRequest() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle(" ");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        MvcResult result = mvc
                .perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }

    @Test
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void postNullDescription_getBadRequest() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        MvcResult result = mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }

    @Test
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void postNullTags_getBadRequest() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");

        MvcResult result = mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(MethodArgumentNotValidException.class.isAssignableFrom(result.getResolvedException().getClass()));

    }

    @Test
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {NEW_QUESTION_ADDED, TWO_UNIQUE_TAGS_ADDED, TWO_UNIQUE_TAG_QUESTION_LINKS_ADDED, USER_ENTITY, ROLE_ENTITY})
    public void postTagsWithUniqueId_checkNewTagsAdded() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("question title");
        TagDto tag1 = new TagDto();
        TagDto tag2 = new TagDto();
        tag1.setName("tagName0");
        tag2.setName("tagName1");
        questionCreateDto.setTags(List.of(tag1, tag2));

        mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {NEW_QUESTION_ADDED, TWO_EXISTENT_TAGS_ADDED, TWO_EXISTENT_TAG_QUESTION_LINKS_ADDED, USER_ENTITY, ROLE_ENTITY})
    public void postTagsWithExistentId_checkNewTagsAdded() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("question title");
        TagDto tag1 = new TagDto();
        TagDto tag2 = new TagDto();
        tag1.setName("db_architecture"); //existed
        tag2.setName("tagName1");
        questionCreateDto.setTags(List.of(tag1, tag2));

        mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    @DataSet(value = {USER_ADD, ROLE_ENTITY, QUESTION_ADD}, disableConstraints = true)
    public void shouldUpVote() throws Exception {

        String authToken = getToken("user102@user.ru", "user");

        mvc.perform(post(urlUpVote).header(AUTH_HEADER, PREFIX + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        VoteQuestion vt = em.createQuery("select vt from VoteQuestion vt " +
                "where vt.user.id = 102 and vt.question.id = 100", VoteQuestion.class).getSingleResult();
        assertThat(vt).isNotNull();
        assertThat(vt.getVote()).isEqualTo(VoteType.UP_VOTE);

        Reputation rt = em.createQuery("select rt from Reputation rt " +
                "where rt.question.id = 100 and rt.sender.id = 102", Reputation.class).getSingleResult();
        assertThat(rt).isNotNull();
        assertThat(rt.getCount()).isEqualTo(10);

        mvc.perform(post(urlUpVote).header(AUTH_HEADER, PREFIX + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DataSet(value = {USER_ADD, ROLE_ENTITY, QUESTION_ADD}, disableConstraints = true)
    public void shouldDownVote() throws Exception {

        String authToken = getToken("user103@user.ru", "user");

        mvc.perform(post(urlDownVote).header(AUTH_HEADER, PREFIX + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        VoteQuestion vt = em.createQuery("select vt from VoteQuestion vt " +
                "where vt.user.id = 103 and vt.question.id = 100", VoteQuestion.class).getSingleResult();
        assertThat(vt).isNotNull();
        assertThat(vt.getVote()).isEqualTo(VoteType.DOWN_VOTE);

        Reputation rt = em.createQuery("select rt from Reputation rt " +
                "where rt.question.id = 100 and rt.sender.id = 103", Reputation.class).getSingleResult();
        assertThat(rt).isNotNull();
        assertThat(rt.getCount()).isEqualTo(-5);

        mvc.perform(post(urlUpVote).header(AUTH_HEADER, PREFIX + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/countShouldBeThree/Question.yml",
            "dataset/QuestionResourceController/countShouldBeThree/user.yml",
            "dataset/QuestionResourceController/countShouldBeThree/role.yml"}, disableConstraints = true)
    public void countShouldBeThree() throws Exception {

        ResultActions response = mvc.perform(get(url + "/count").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("3"));

    }
}