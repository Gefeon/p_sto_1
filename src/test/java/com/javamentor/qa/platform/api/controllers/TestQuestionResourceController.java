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
import static org.hamcrest.Matchers.*;
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

    private static final String ANSWER_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/answer.yml";
    private static final String QUESTION_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/question.yml";
    private static final String QUESTION_TAG_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/question_has_tag.yml";
    private static final String REPUTATION_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/reputation.yml";
    private static final String TAG_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/tag.yml";
    private static final String ROLE_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/role.yml";
    private static final String USER_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/user.yml";
    private static final String VOTE_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/vote.yml";
    private static final String IGNORE_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/ignored.yml";
    private static final String TRACK_BY_DATE = "dataset/QuestionResourceController/getQuestionDtoByDate/tracked.yml";

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

    /*
    * Пагинация вопросов по дате, сначала свежие
    * */
    @Test
    @DataSet(value = {ANSWER_BY_DATE, QUESTION_BY_DATE, QUESTION_TAG_BY_DATE, REPUTATION_BY_DATE, TAG_BY_DATE,
            ROLE_BY_DATE, USER_BY_DATE, VOTE_BY_DATE, IGNORE_BY_DATE, TRACK_BY_DATE}, disableConstraints = true)
    public void getQuestionsByPersistDate() throws Exception {

        String token = getToken("user100@user.ru", "user");

        // стандартный запрос
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 112, 110, 108,106, 104, 102, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is( 105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is( 100)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-26T00:00:00")))
                .andExpect(jsonPath("$.totalResultCount", is(8)));

        // нет обязательного параметра - текущей страницы
        mvc.perform(get("/api/user/question/new?items=16&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());

        // запрос на большее кол-во данных чем есть
        mvc.perform(get("/api/user/question/new?currPage=2&items=300&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items").isEmpty());

        // текущая страница велика
        mvc.perform(get("/api/user/question/new?currPage=200&items=3&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items").isEmpty());

        // нет ignoredTags параметров или не существует ignoredTag с заданным Id
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 113, 112, 110, 109, 108, 106, 104, 102, 101, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is(105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is(104)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-27T00:00:00")))
                .andExpect(jsonPath("$.totalResultCount", is(11)));

        // нет trackedTags параметров
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&ignoredTags=101,106,107,108,109").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 112, 110, 108,106, 104, 102, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is( 105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is( 100)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-26T00:00:00")))
                .andExpect(jsonPath("$.totalResultCount", is(8)));

        // нет необязательных параметров, вывод 10 значений по умолчанию
        mvc.perform(get("/api/user/question/new?currPage=1").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(115, 114, 113, 112, 111, 110, 109, 108, 107, 106)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is(109)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is(105)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-29T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.totalResultCount", is(16)));

        // произвольные параметры
        mvc.perform(get("/api/user/question/new?currPage=1&items=4&ignoredTags=101&trackedTags=103").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder( 109, 108)))
                .andExpect(jsonPath("$.items[0].listTagDto[*].id").value(containsInAnyOrder(100, 103, 108)))
                .andExpect(jsonPath("$.items[1].listTagDto[*].id").value(containsInAnyOrder(100, 103, 104)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-23T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-22T00:00:00")))
                .andExpect(jsonPath("$.totalResultCount", is(2)));
    }
}
