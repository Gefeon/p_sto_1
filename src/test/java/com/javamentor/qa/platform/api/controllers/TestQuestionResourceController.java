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

import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestQuestionResourceController extends AbstractTestApi {

    private final String url = "/api/user/question";
    private final String url1 = "/api/user/question/100";

    private final String urlUpVote = "/api/user/question/100/upVote";
    private final String urlDownVote = "/api/user/question/100/downVote";

    private static final String USER_ENTITY = "dataset/QuestionResourceController/user.yml";
    private static final String USER_ENTITY_PAGINATION = "dataset/QuestionResourceController/allQuestuionDtos/user.yml";
    private static final String ROLE_ENTITY = "dataset/QuestionResourceController/role.yml";
    private static final String QUESTION_ENTITY = "dataset/QuestionResourceController/question.yml";
    private static final String QUESTION_ENTITY_PAGINATION = "dataset/QuestionResourceController/allQuestuionDtos/question.yml";
    private static final String TAG_ENTITY = "dataset/QuestionResourceController/tag.yml";
    private static final String TAG_ENTITY_PAGINATION = "dataset/QuestionResourceController/allQuestuionDtos/tag.yml";
    private static final String QUESTION_HAS_TAG_ENTITY_PAGINATION = "dataset/QuestionResourceController/allQuestuionDtos/questionHasTag.yml";
    private static final String QUESTION_HAS_TAG_ENTITY = "dataset/QuestionResourceController/questionHasTag.yml";
    private static final String USER_ADD = "dataset/QuestionResourceController/UserAdd.yml";
    private static final String QUESTION_ADD = "dataset/QuestionResourceController/QuestionAdd.yml";
    private static final String ANSWER_ENTITY = "dataset/QuestionResourceController/Answer.yml";
    private static final String QUESTION_VIEWED_ENTITY = "dataset/QuestionResourceController/QuestionViewed.yml";
    private static final String REPUTATION_ENTITY = "dataset/QuestionResourceController/Reputation.yml";
    private static final String VOTE_QUESTION_ENTITY = "dataset/QuestionResourceController/QuestionVote.yml";
    private static final String ANSWER_ENTITY_PAGINATION = "dataset/QuestionResourceController/allQuestuionDtos/answer.yml";
    private static final String VOTE_QUESTION_ENTITY1 = "dataset/QuestionResourceController/allQuestuionDtos/voteQuestion.yml";

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

    //проверка что находятся все вопросы, в которых есть тэг хотя бы один из TrackedTags, без дубликатов.
    // TagDto для каждого вопроса достаются именно те, которые связаны с этим вопросом
    // IgnoredTag выбраны из несуществующих в БД id
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoExistentTrackedTagsNonexistentIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=101&trackedId=100&ignoredId=200&ignoredId=201").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(5)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(100, 101, 103, 106, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(5), hasSize(1), hasSize(5), hasSize(2), hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(100, 101, 102, 103, 104, 100, 100, 101, 102, 103, 104, 106, 100, 100, 105, 109)));
    }

    //проверка что находятся все вопросы, с которыми нет ни одного связанного тэга из IgnoredTags, без дубликатов.
    // TagDto для каждого вопроса достаются именно те, которые связаны с этим вопросом
    // trackedTag - все в БД id
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNonexistentTrackedTagsExistentIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&ignoredId=100&ignoredId=101&ignoredId=103").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(9)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(102, 104, 105, 107, 108, 109, 110, 111, 113)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(2), hasSize(2), hasSize(1), hasSize(4), hasSize(1), hasSize(1), hasSize(2), hasSize(1), hasSize(1))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(106, 107, 111, 114, 114, 111, 112, 113, 114, 105, 114, 109, 108, 113, 111)));
    }

    //проверка что находятся все вопросы, в которых есть тэг хотя бы один из trackedTags И с которыми нет ни одного связанного тэга из ignoredTags, без дубликатов.
    // TagDto для каждого вопроса достаются именно те, которые связаны с этим вопросом
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoIntersectTrackedTagsAndIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=100&trackedId=101&trackedId=109&trackedId=114&ignoredId=102&ignoredId=106&ignoredId=108&ignoredId=111").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(4)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(101, 105, 109, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1), hasSize(1), hasSize(1), hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(100, 114, 114, 100, 105, 109)));
    }

    //проверка что находятся все вопросы, в которых есть тэг хотя бы один из trackedTags И с которыми нет ни одного связанного тэга из ignoredTags, без дубликатов.
    //вопросы, содержащие тэги из trackedTags не пересекаются с вопросами, содержащими тэги из ignoredTags
    // trackedTag и ignoredTag частично из тех, что есть в БД и из тех, которых нет в БД
    // TagDto для каждого вопроса достаются именно те, которые связаны с этим вопросом
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNonIntersectTrackedTagsAndIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=105&trackedId=200&ignoredId=111&ignoredId=200").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(2)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(108, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1), hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(105, 100, 105, 109)));
    }


    // trackedTag не передавать, полагается что trackedTags - все записи в БД
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoWithoutTrackedTags_expectedAllTagsWithoutIgnored() throws Exception {
        mvc.perform(get(url + "?currPage=1&ignoredId=100&ignoredId=102&ignoredId=103").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(9)));
    }

    // ignoredTag не передавать, полагается что ничего не игнорируется
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoWithoutIgnoredTags_expectedAllTrackedTags() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=100&trackedId=101&trackedId=102&trackedId=113").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(7)));
    }

    //  передать в trackedTag отрицательное число
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNegativeTrackedTags_expectedBadRequest() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=103&trackedId=-103").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //  передать в trackedTag и ignoredTag одинаковые числа, ожидать как было бы без дубликатов
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoRepetetiveTrackedAndIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=105&trackedId=105&ignoredId=111&ignoredId=111").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(2)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(108, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1), hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(105, 100, 105, 109)));
    }

    //  передать в trackedTag тэг, с которым не связано ни одного вопроса и ожидать пустой список
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNoSuchTrackedTagsInQuestions_expectedEmptyData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=110").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(0)));
    }

    //  передать в ignoredTag тэг, с которым не связано ни одного вопроса и ожидать все в БД
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNoSuchIgnoredTagsInQuestions_expectedAllData() throws Exception {
        mvc.perform(get(url + "?currPage=1&ignoredId=110&items=100").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(20)));
    }

    // тест пагинации и корректности выводимых данных для репутации, голосования за вопрос, подсчёта ответов
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY, VOTE_QUESTION_ENTITY1}, disableConstraints = true)
    public void getAllQuestionDtoPaginationCheck_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=2&ignoredId=100&ignoredId=101&ignoredId=103&items=4").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(4)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(108, 109, 110, 111)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1), hasSize(1), hasSize(2), hasSize(1))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(105, 114, 109, 108, 113)))
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(4))
                .andExpect(jsonPath("$.totalPageCount").value(3))
                .andExpect(jsonPath("$.totalResultCount").value(9))
                .andExpect(jsonPath("$.items[*].countAnswer").value(containsInRelativeOrder(2, 1, 0, 0)))
                .andExpect(jsonPath("$.items[*].authorReputation").value(containsInRelativeOrder(30, 30, 30, -5)))
                .andExpect(jsonPath("$.items[*].countValuable").value(containsInRelativeOrder(2, -2, 1, -1)));
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
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 112, 110, 108, 106, 104, 102, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is(105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is(100)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-26T00:00:00")))
                .andExpect(jsonPath("$.items.length()", is(8)))
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
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 113, 112, 110, 109, 108, 106, 104, 102, 101, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is(105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is(104)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-27T00:00:00")))
                .andExpect(jsonPath("$.items.length()", is(11)))
                .andExpect(jsonPath("$.totalResultCount", is(11)));

        // нет trackedTags параметров
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&ignoredTags=101,106,107,108,109").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 112, 110, 108, 106, 104, 102, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is(105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is(100)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-26T00:00:00")))
                .andExpect(jsonPath("$.items.length()", is(8)))
                .andExpect(jsonPath("$.totalResultCount", is(8)));

        // нет необязательных параметров, вывод 10 значений по умолчанию
        mvc.perform(get("/api/user/question/new?currPage=1").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(115, 114, 113, 112, 111, 110, 109, 108, 107, 106)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is(109)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is(105)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-29T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items.length()", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(16)));

        // произвольные параметры
        mvc.perform(get("/api/user/question/new?currPage=1&items=4&ignoredTags=101&trackedTags=103").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(109, 108)))
                .andExpect(jsonPath("$.items[0].listTagDto[*].id").value(containsInAnyOrder(100, 103, 108)))
                .andExpect(jsonPath("$.items[1].listTagDto[*].id").value(containsInAnyOrder(100, 103, 104)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-23T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-22T00:00:00")))
                .andExpect(jsonPath("$.items.length()", is(2)))
                .andExpect(jsonPath("$.totalResultCount", is(2)));
    }

    @Test
    @DataSet(value = {USER_ENTITY,
            ROLE_ENTITY,
            ANSWER_ENTITY,
            QUESTION_ENTITY,
            QUESTION_VIEWED_ENTITY,
            REPUTATION_ENTITY,
            TAG_ENTITY,
            VOTE_QUESTION_ENTITY,
            QUESTION_HAS_TAG_ENTITY}, disableConstraints = true)
    public void getQuestionDtoById() throws Exception {

        String token = getToken("user100@user.ru", "user");

        mvc.perform(get(url1).header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.title", is("lazyEx")))
                .andExpect(jsonPath("$.authorId", is(100)))
                .andExpect(jsonPath("$.authorReputation", is(1)))
                .andExpect(jsonPath("$.authorName", is("ИВАНОВ ИВАН ИВАНОВИЧ")))
                .andExpect(jsonPath("$.authorImage", is("test.ru")))
                .andExpect(jsonPath("$.description", is("fix lazyInitialization Exception")))
                .andExpect(jsonPath("$.viewCount", is(0)))
                .andExpect(jsonPath("$.countAnswer", is(1)))
                .andExpect(jsonPath("$.countValuable", is(1)))
                .andExpect(jsonPath("$.listTagDto.[*].id", containsInAnyOrder(100, 101)))
                .andExpect(jsonPath("$.listTagDto.[*].name", containsInAnyOrder("db_architecture", "Room")))
                .andExpect(jsonPath("$.listTagDto.[*].description", containsInAnyOrder("my sql database architecture", "Room Android best practises")));

        /*
         * Проверка на не существующий ID
         * */
        mvc.perform(get("/api/user/question/1000").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Missing question or invalid id"));
    }
}
