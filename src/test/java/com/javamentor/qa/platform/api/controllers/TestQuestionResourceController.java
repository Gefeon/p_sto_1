package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

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

    private static final String USER_ENTITY = "dataset/questionResourceController/user.yml";
    private static final String USER_ENTITY_PAGINATION = "dataset/questionResourceController/allQuestuionDtos/user.yml";
    private static final String ROLE_ENTITY = "dataset/questionResourceController/role.yml";
    private static final String QUESTION_ENTITY = "dataset/questionResourceController/question.yml";
    private static final String ANSWER_ENTITY = "dataset/questionResourceController/answer.yml";
    private static final String QUESTION_ENTITY_PAGINATION = "dataset/questionResourceController/allQuestuionDtos/question.yml";
    private static final String QUESTION_PAGINATION_BY_TAG = "dataset/questionResourceController/paginationByTag/question.yml";
    private static final String TAG_ENTITY = "dataset/questionResourceController/tag.yml";
    private static final String REPUTATION_ENTITY = "dataset/questionResourceController/reputation.yml";
    private static final String REPUTATION_ENTITY_PAGINATION = "dataset/questionResourceController/allQuestuionDtos/reputation.yml";
    private static final String VOTEQUESTION_ENTITY = "dataset/questionResourceController/vote_question.yml";
    private static final String TAG_ENTITY_PAGINATION = "dataset/questionResourceController/allQuestuionDtos/tag.yml";
    private static final String QUESTION_HAS_TAG_ENTITY_PAGINATION = "dataset/questionResourceController/allQuestuionDtos/question_has_tag.yml";
    private static final String QUESTION_HAS_TAG_PAGINATION_BY_TAG = "dataset/questionResourceController/paginationByTag/question_has_tag.yml";
    private static final String QUESTION_HAS_TAG_ENTITY = "dataset/questionResourceController/question_has_tag.yml";
    private static final String USER_ADD = "dataset/questionResourceController/user_add.yml";
    private static final String QUESTION_ADD = "dataset/questionResourceController/question_add.yml";
    private static final String QUESTION_VIEWED_ENTITY = "dataset/questionResourceController/question_viewed.yml";
    private static final String VOTE_QUESTION_ENTITY = "dataset/questionResourceController/question_vote.yml";
    private static final String ANSWER_ENTITY_PAGINATION = "dataset/questionResourceController/allQuestuionDtos/answer.yml";
    private static final String VOTE_QUESTION_ENTITY1 = "dataset/questionResourceController/allQuestuionDtos/vote_question.yml";
    private static final String COMMENT_ENTITY = "dataset/questionResourceController/Comment.yml";
    private static final String REPUTATION_COMMENT_ENTITY = "dataset/questionResourceController/reputationComment.yml";

    private static final String NEW_QUESTION_ADDED = "dataset/expected/resourceQuestionController/new_question_added.yml";
    private static final String THREE_TAGS_ADDED = "dataset/expected/resourceQuestionController/three_tags_added.yml";
    private static final String THREE_TAG_QUESTION_LINKS_ADDED = "dataset/expected/resourceQuestionController/three_question_has_tags_added.yml";
    private static final String TWO_UNIQUE_TAGS_ADDED = "dataset/expected/resourceQuestionController/two_unique_id_tags_added.yml";
    private static final String TWO_UNIQUE_TAG_QUESTION_LINKS_ADDED = "dataset/expected/resourceQuestionController/two_question_has_tags_added.yml";
    private static final String TWO_EXISTENT_TAGS_ADDED = "dataset/expected/resourceQuestionController/two_existent_id_tags_added.yml";
    private static final String TWO_EXISTENT_TAG_QUESTION_LINKS_ADDED = "dataset/expected/resourceQuestionController/two_existent_id_question_has_tag_added.yml";

    private static final String ANSWER_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/answer.yml";
    private static final String QUESTION_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/question.yml";
    private static final String QUESTION_TAG_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/question_has_tag.yml";
    private static final String REPUTATION_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/reputation.yml";
    private static final String TAG_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/tag.yml";
    private static final String ROLE_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/role.yml";
    private static final String USER_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/user.yml";
    private static final String VOTE_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/vote.yml";
    private static final String IGNORE_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/ignored.yml";
    private static final String TRACK_BY_DATE = "dataset/questionResourceController/getQuestionDtoByDate/tracked.yml";

    private static final String ANSWER_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/answer.yml";
    private static final String IGNORED_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/ignored.yml";
    private static final String QUESTION_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/question.yml";
    private static final String QUESTION_TAG_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/question_has_tag.yml";
    private static final String REPUTATION_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/reputation.yml";
    private static final String ROLE_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/role.yml";
    private static final String USER_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/user.yml";
    private static final String TAG_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/tag.yml";
    private static final String VOTE_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/vote.yml";
    private static final String TRACK_NO_ANSWER = "dataset/questionResourceController/questionsNoAnswer/tracked.yml";

    private static final String USER_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/user.yml";
    private static final String ROLE_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/role.yml";
    private static final String ANSWER_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/answer.yml";
    private static final String QUESTION_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/question.yml";
    private static final String QUESTION_VIEWED_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/question_viewed.yml";
    private static final String REPUTATION_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/reputation.yml";
    private static final String TAG_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/tag.yml";
    private static final String VOTE_QUESTION_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/question_vote.yml";
    private static final String QUESTION_HAS_TAG_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/question_has_tag.yml";
    private static final String COMMENT_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/comment.yml";
    private static final String COMMENT_QUESTION_HAS_TAG_ENTITY_WITH_COMMENT = "dataset/questionResourceController/getQuestionDtoByIdWithComment/comment_question.yml";

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

       mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void postNullDescription_getBadRequest() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("title");
        TagDto tag = new TagDto();
        tag.setName("tagName");
        questionCreateDto.setTags(List.of(tag));

        mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @DataSet(value = {QUESTION_ENTITY, TAG_ENTITY, QUESTION_HAS_TAG_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    public void postNullTags_getBadRequest() throws Exception {
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("question description");
        questionCreateDto.setTitle("title");

        mvc.perform(post(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user"))
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
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

        String urlDownVote = "/api/user/question/100/downVote";
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

    //???????????????? ?????? ?????????????????? ?????? ??????????????, ?? ?????????????? ???????? ?????? ???????? ???? ???????? ???? TrackedTags, ?????? ????????????????????.
    // TagDto ?????? ?????????????? ?????????????? ?????????????????? ???????????? ????, ?????????????? ?????????????? ?? ???????? ????????????????
    // IgnoredTag ?????????????? ???? ???????????????????????????? ?? ???? id
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoExistentTrackedTagsNonexistentIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=101&trackedId=100&ignoredId=200&ignoredId=201").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(5)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(100, 101, 103, 106, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(5),hasSize(1),hasSize(5),hasSize(2),hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(100,101,102,103,104,100,100,101,102,103,104,106,100,100,105,109)))
                .andExpect(jsonPath("$.items[0].isUserVote", is( "UP_VOTE")));
    }

    //???????????????? ?????? ?????????????????? ?????? ??????????????, ?? ???????????????? ?????? ???? ???????????? ???????????????????? ???????? ???? IgnoredTags, ?????? ????????????????????.
    // TagDto ?????? ?????????????? ?????????????? ?????????????????? ???????????? ????, ?????????????? ?????????????? ?? ???????? ????????????????
    // trackedTag - ?????? ?? ???? id
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNonexistentTrackedTagsExistentIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&ignoredId=100&ignoredId=101&ignoredId=103").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(9)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(102, 104, 105, 107, 108, 109, 110, 111, 113)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(2),hasSize(2),hasSize(1),hasSize(4),hasSize(1),hasSize(1),hasSize(2),hasSize(1),hasSize(1))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(106,107,111,114,114,111,112,113,114,105,114,109,108,113,111)));
    }

    //???????????????? ?????? ?????????????????? ?????? ??????????????, ?? ?????????????? ???????? ?????? ???????? ???? ???????? ???? trackedTags ?? ?? ???????????????? ?????? ???? ???????????? ???????????????????? ???????? ???? ignoredTags, ?????? ????????????????????.
    // TagDto ?????? ?????????????? ?????????????? ?????????????????? ???????????? ????, ?????????????? ?????????????? ?? ???????? ????????????????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoIntersectTrackedTagsAndIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=100&trackedId=101&trackedId=109&trackedId=114&ignoredId=102&ignoredId=106&ignoredId=108&ignoredId=111").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(4)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(101, 105, 109, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1),hasSize(1),hasSize(1),hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(100,114,114,100,105,109)));
    }

    //???????????????? ?????? ?????????????????? ?????? ??????????????, ?? ?????????????? ???????? ?????? ???????? ???? ???????? ???? trackedTags ?? ?? ???????????????? ?????? ???? ???????????? ???????????????????? ???????? ???? ignoredTags, ?????? ????????????????????.
    //??????????????, ???????????????????? ???????? ???? trackedTags ???? ???????????????????????? ?? ??????????????????, ?????????????????????? ???????? ???? ignoredTags
    // trackedTag ?? ignoredTag ???????????????? ???? ??????, ?????? ???????? ?? ???? ?? ???? ??????, ?????????????? ?????? ?? ????
    // TagDto ?????? ?????????????? ?????????????? ?????????????????? ???????????? ????, ?????????????? ?????????????? ?? ???????? ????????????????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNonIntersectTrackedTagsAndIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=105&trackedId=200&ignoredId=111&ignoredId=200").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(2)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(108, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1),hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(105,100,105,109)));
    }

    // trackedTag ???? ????????????????????, ???????????????????? ?????? trackedTags - ?????? ???????????? ?? ????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoWithoutTrackedTags_expectedAllTagsWithoutIgnored() throws Exception {
        mvc.perform(get(url + "?currPage=1&ignoredId=100&ignoredId=102&ignoredId=103").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(9)));
    }

    // ignoredTag ???? ????????????????????, ???????????????????? ?????? ???????????? ???? ????????????????????????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoWithoutIgnoredTags_expectedAllTrackedTags() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=100&trackedId=101&trackedId=102&trackedId=113").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(7)))
                .andExpect(jsonPath("$.items[0].isUserVote", is( "UP_VOTE")));
    }

    //  ???????????????? ?? trackedTag ?????????????????????????? ??????????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNegativeTrackedTags_expectedBadRequest() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=103&trackedId=-103").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //  ???????????????? ?? trackedTag ?? ignoredTag ???????????????????? ??????????, ?????????????? ?????? ???????? ???? ?????? ????????????????????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoRepetetiveTrackedAndIgnoredTags_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=105&trackedId=105&ignoredId=111&ignoredId=111").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(2)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(108, 112)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1),hasSize(3))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(105,100,105,109)));
    }

    //  ???????????????? ?? trackedTag ??????, ?? ?????????????? ???? ?????????????? ???? ???????????? ?????????????? ?? ?????????????? ???????????? ????????????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNoSuchTrackedTagsInQuestions_expectedEmptyData() throws Exception {
        mvc.perform(get(url + "?currPage=1&trackedId=110").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(0)));
    }

    //  ???????????????? ?? ignoredTag ??????, ?? ?????????????? ???? ?????????????? ???? ???????????? ?????????????? ?? ?????????????? ?????? ?? ????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY}, disableConstraints = true)
    public void getAllQuestionDtoNoSuchIgnoredTagsInQuestions_expectedAllData() throws Exception {
        mvc.perform(get(url + "?currPage=1&ignoredId=110&items=100").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(20)));
    }

    // ???????? ?????????????????? ?? ???????????????????????? ?????????????????? ???????????? ?????? ??????????????????, ?????????????????????? ???? ????????????, ???????????????? ??????????????
    @Test
    @DataSet(value = {QUESTION_ENTITY_PAGINATION, USER_ENTITY_PAGINATION, ROLE_ENTITY, ANSWER_ENTITY_PAGINATION, TAG_ENTITY_PAGINATION, QUESTION_HAS_TAG_ENTITY_PAGINATION, REPUTATION_ENTITY_PAGINATION, VOTE_QUESTION_ENTITY1}, disableConstraints = true)
    public void getAllQuestionDtoPaginationCheck_expectedCorrectData() throws Exception {
        mvc.perform(get(url + "?currPage=2&ignoredId=100&ignoredId=101&ignoredId=103&items=4").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").value(hasSize(4)))
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder( 108, 109, 110, 111)))
                .andExpect(jsonPath("$.items[*].listTagDto").value(containsInRelativeOrder(hasSize(1),hasSize(1),hasSize(2),hasSize(1))))
                .andExpect(jsonPath("$.items[*].listTagDto[*].id").value(containsInAnyOrder(105,114,109,108,113)))
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.itemsOnPage").value(4))
                .andExpect(jsonPath("$.totalPageCount").value(3))
                .andExpect(jsonPath("$.totalResultCount").value(9))
                .andExpect(jsonPath("$.items[*].countAnswer").value(containsInRelativeOrder( 2, 1, 0, 0)))
                .andExpect(jsonPath("$.items[*].authorReputation").value(containsInRelativeOrder( 30, 30, 30, -5)))
                .andExpect(jsonPath("$.items[*].countValuable").value(containsInRelativeOrder( 2, -2, 1, -1)))
                .andExpect(jsonPath("$.items[1].isUserVote", is( "UP_VOTE")));
    }

    @Test
    @DataSet(value = {"dataset/questionResourceController/countShouldBeThree/Question.yml",
            "dataset/questionResourceController/countShouldBeThree/user.yml",
            "dataset/questionResourceController/countShouldBeThree/role.yml"}, disableConstraints = true)
    public void countShouldBeThree() throws Exception {
        ResultActions response = mvc.perform(get(url + "/count").header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("3"));

    }

    @Test
    @DataSet(value = {USER_ENTITY, ROLE_ENTITY, QUESTION_PAGINATION_BY_TAG, TAG_ENTITY, QUESTION_HAS_TAG_PAGINATION_BY_TAG, ANSWER_ENTITY, REPUTATION_ENTITY, VOTEQUESTION_ENTITY}, disableConstraints = true)
    public void getQuestionDtoByTag() throws Exception {
        String authToken = getToken("user100@user.ru", "user");
        ResultActions response = mvc.perform(get("/api/user/question/tag/101?currPage=1&items=5").header(AUTH_HEADER, PREFIX + authToken));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(2)))
                .andExpect(jsonPath("$.itemsOnPage", is(5)))
                .andExpect(jsonPath("$.totalResultCount", is(6)))
                .andExpect(jsonPath("$.items[0].id", is(100)))
                .andExpect(jsonPath("$.items[0].title", is("lazyEx")))
                .andExpect(jsonPath("$.items[0].authorId", is(100)))
                .andExpect(jsonPath("$.items[0].authorName", is("???????????? ???????? ????????????????")))
                .andExpect(jsonPath("$.items[0].authorImage", is("test.ru")))
                .andExpect(jsonPath("$.items[0].description", is("fix lazyInitialization Exception")))
                .andExpect(jsonPath("$.items[0].viewCount", is(0)))
                .andExpect(jsonPath("$.items[0].countAnswer", is(1)))
                .andExpect(jsonPath("$.items[0].countValuable", is(1)))
                .andExpect(jsonPath("$.items[0].authorReputation", is(10)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-30T00:29:29.62381")))
                .andExpect(jsonPath("$.items[0].lastUpdateDateTime", is("2021-11-30T00:29:29.62381")))
                .andExpect(jsonPath("$.items[0].listTagDto.[0].id", is(100)))
                .andExpect(jsonPath("$.items[0].listTagDto.[0].name", is("db_architecture")))
                .andExpect(jsonPath("$.items[0].listTagDto.[0].description", is("my sql database architecture")));

        response = mvc.perform(get("/api/user/question/tag/100?currPage=2&items=4").header(AUTH_HEADER, PREFIX + authToken));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(2)))
                .andExpect(jsonPath("$.totalPageCount", is(4)))
                .andExpect(jsonPath("$.itemsOnPage", is(4)))
                .andExpect(jsonPath("$.totalResultCount", is(13)));

        response = mvc.perform(get("/api/user/question/tag/99?currPage=1&items=1").header(AUTH_HEADER, PREFIX + authToken));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.currentPageNumber", is(1)))
                .andExpect(jsonPath("$.totalPageCount", is(1)))
                .andExpect(jsonPath("$.itemsOnPage", is(1)))
                .andExpect(jsonPath("$.totalResultCount", is(0)));

    }

    /*
     * ?????????????????? ???????????????? ???? ????????, ?????????????? ????????????
     * */
    @Test
    @DataSet(value = {ANSWER_BY_DATE, QUESTION_BY_DATE, QUESTION_TAG_BY_DATE, REPUTATION_BY_DATE, TAG_BY_DATE,
            ROLE_BY_DATE, USER_BY_DATE, VOTE_BY_DATE, IGNORE_BY_DATE, TRACK_BY_DATE}, disableConstraints = true)
    public void getQuestionsByPersistDate() throws Exception {

        String token = getToken("user100@user.ru", "user");

        // ?????????????????????? ????????????
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 112, 110, 108,106, 104, 102, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is( 105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is( 100)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-26T00:00:00")))
                .andExpect(jsonPath("$.items.length()", is(8)))
                .andExpect(jsonPath("$.totalResultCount", is(8)))
                .andExpect(jsonPath("$.items[7].isUserVote", is( "UP_VOTE")));

        // ?????? ?????????????????????????? ?????????????????? - ?????????????? ????????????????
        mvc.perform(get("/api/user/question/new?items=16&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());

        // ???????????? ???? ?????????????? ??????-???? ???????????? ?????? ????????
        mvc.perform(get("/api/user/question/new?currPage=2&items=300&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items").isEmpty());

        // ?????????????? ???????????????? ????????????
        mvc.perform(get("/api/user/question/new?currPage=200&items=3&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items").isEmpty());

        // ?????? ignoredTags ???????????????????? ?????? ???? ???????????????????? ignoredTag ?? ???????????????? Id
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX +  token))
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

        // ?????? trackedTags ????????????????????
        mvc.perform(get("/api/user/question/new?currPage=1&items=16&ignoredTags=101,106,107,108,109").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder(114, 112, 110, 108,106, 104, 102, 100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", is( 105)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", is( 100)))
                .andExpect(jsonPath("$.items[0].persistDateTime", is("2021-11-28T00:00:00")))
                .andExpect(jsonPath("$.items[1].persistDateTime", is("2021-11-26T00:00:00")))
                .andExpect(jsonPath("$.items.length()", is(8)))
                .andExpect(jsonPath("$.totalResultCount", is(8)));

        // ?????? ???????????????????????????? ????????????????????, ?????????? 10 ???????????????? ???? ??????????????????
        mvc.perform(get("/api/user/question/new?currPage=1").header(AUTH_HEADER, PREFIX +  token))
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

        // ???????????????????????? ??????????????????
        mvc.perform(get("/api/user/question/new?currPage=1&items=4&ignoredTags=101&trackedTags=103").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items[*].id").value(containsInRelativeOrder( 109, 108)))
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

        String url1 = "/api/user/question/100";
        mvc.perform(get(url1).header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.title", is("lazyEx")))
                .andExpect(jsonPath("$.authorId", is(100)))
                .andExpect(jsonPath("$.authorReputation", is(1)))
                .andExpect(jsonPath("$.authorName", is("???????????? ???????? ????????????????")))
                .andExpect(jsonPath("$.authorImage", is("test.ru")))
                .andExpect(jsonPath("$.description", is("fix lazyInitialization Exception")))
                .andExpect(jsonPath("$.viewCount", is(0)))
                .andExpect(jsonPath("$.countAnswer", is(1)))
                .andExpect(jsonPath("$.countValuable", is(2)))
                .andExpect(jsonPath("$.listTagDto.[*].id", containsInAnyOrder(100, 101)))
                .andExpect(jsonPath("$.listTagDto.[*].name", containsInAnyOrder("db_architecture", "Room")))
                .andExpect(jsonPath("$.listTagDto.[*].description", containsInAnyOrder("my sql database architecture", "Room Android best practises")))
                .andExpect(jsonPath("$.isUserVote", is("UP_VOTE")));

        /*
         * ???????????????? ???? ???? ???????????????????????? ID
         * */
        mvc.perform(get("/api/user/question/1000").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Missing question or invalid id"));
    }

    @Test
    @DataSet(value = {USER_ENTITY,
            ROLE_ENTITY,
            COMMENT_ENTITY,
            QUESTION_ENTITY,
            REPUTATION_COMMENT_ENTITY,
    }, disableConstraints = true)
    public void getQuestionCommentDtoById() throws Exception {

        String token = getToken("user100@user.ru", "user");

        String urlComment = "/api/user/question/100/comment";
        mvc.perform(get(urlComment).header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", containsInAnyOrder(100)))
                .andExpect(jsonPath("$.[*].questionId", containsInAnyOrder(100)))
                .andExpect(jsonPath("$.[*].lastRedactionDate", containsInAnyOrder("2021-11-30T00:29:29")))
                .andExpect(jsonPath("$.[*].persistDate", containsInAnyOrder("2021-11-30T00:29:29")))
                .andExpect(jsonPath("$.[*].text", containsInAnyOrder("fix lazyInitialization Exception")))
                .andExpect(jsonPath("$.[*].userId", containsInAnyOrder(100)))
                .andExpect(jsonPath("$.[*].imageLink", containsInAnyOrder("test.ru")))
                .andExpect(jsonPath("$.[*].reputation", containsInAnyOrder(65)));
    }

    /*
     * ?????????????????? ???????????????? ?????? ??????????????
     * */
    @Test
    @DataSet(value = {ANSWER_NO_ANSWER, IGNORED_NO_ANSWER, QUESTION_NO_ANSWER, QUESTION_TAG_NO_ANSWER, REPUTATION_NO_ANSWER,
            ROLE_NO_ANSWER, USER_NO_ANSWER, TAG_NO_ANSWER, VOTE_NO_ANSWER, TRACK_NO_ANSWER}, disableConstraints = true)
    public void getQuestionsDtoNoAnswer() throws Exception {

        String token = getToken("user100@user.ru", "user");

        // ?????????????????????? ????????????
        mvc.perform(get("/api/user/question/noAnswer?currPage=1&items=16&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items.length()", is(6)))
                .andExpect(jsonPath("$.totalResultCount", is(6)));


        // ?????? ?????????????????????????? ?????????????????? - ?????????????? ????????????????
        mvc.perform(get("/api/user/question/noAnswer?items=16&ignoredTags=101,106&trackedTags=100,102")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());

        // ???????????? ???? ?????????????? ??????-???? ???????????? ?????? ????????
        mvc.perform(get("/api/user/question/noAnswer?currPage=2&items=300&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items").isEmpty());

        // ?????????????? ???????????????? ????????????
        mvc.perform(get("/api/user/question/noAnswer?currPage=200&items=3&ignoredTags=101,106,107,108,109&trackedTags=100,102,103,104,105")
                        .header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items").isEmpty());

        // ?????? ignoredTags ???????????????????? ?????? ???? ???????????????????? ignoredTag ?? ???????????????? Id
        mvc.perform(get("/api/user/question/noAnswer?currPage=1&items=16&trackedTags=100,102,103,104,105").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items.length()", is(8)))
                .andExpect(jsonPath("$.totalResultCount", is(8)));


        // ?????? trackedTags ????????????????????
        mvc.perform(get("/api/user/question/noAnswer?currPage=1&items=16&ignoredTags=101,106,107,108,109").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items.length()", is(6)))
                .andExpect(jsonPath("$.totalResultCount", is(6)));


        // ?????? ???????????????????????????? ????????????????????, ?????????? 10 ???????????????? ???? ??????????????????
        mvc.perform(get("/api/user/question/noAnswer?currPage=1").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items.length()", is(10)))
                .andExpect(jsonPath("$.totalResultCount", is(13)));

        // ???????????????????????? ??????????????????
        mvc.perform(get("/api/user/question/noAnswer?currPage=1&items=4&ignoredTags=101&trackedTags=103").header(AUTH_HEADER, PREFIX +  token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items.length()", is(2)))
                .andExpect(jsonPath("$.totalResultCount", is(2)));
    }

    @Test
    @DataSet(value = {
            USER_ENTITY_WITH_COMMENT,
            ROLE_ENTITY_WITH_COMMENT,
            ANSWER_ENTITY_WITH_COMMENT,
            QUESTION_ENTITY_WITH_COMMENT,
            QUESTION_VIEWED_ENTITY_WITH_COMMENT,
            REPUTATION_ENTITY_WITH_COMMENT,
            TAG_ENTITY_WITH_COMMENT,
            VOTE_QUESTION_ENTITY_WITH_COMMENT,
            QUESTION_HAS_TAG_ENTITY_WITH_COMMENT,
            COMMENT_ENTITY_WITH_COMMENT,
            COMMENT_QUESTION_HAS_TAG_ENTITY_WITH_COMMENT
    }, disableConstraints = true)
    void getQuestionDtoByIdWithComment() throws Exception {
        String token = getToken("user100@user.ru", "user");

        String urlQuestion = "/api/user/question/100";

        mvc.perform(get(urlQuestion).header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.title", is("lazyEx")))
                .andExpect(jsonPath("$.authorId", is(100)))
                .andExpect(jsonPath("$.authorReputation", is(1)))
                .andExpect(jsonPath("$.authorName", is("???????????? ???????? ????????????????")))
                .andExpect(jsonPath("$.authorImage", is("test.ru")))
                .andExpect(jsonPath("$.description", is("fix lazyInitialization Exception")))
                .andExpect(jsonPath("$.viewCount", is(0)))
                .andExpect(jsonPath("$.countAnswer", is(1)))
                .andExpect(jsonPath("$.countValuable", is(0)))
                .andExpect(jsonPath("$.listTagDto.[*].id", containsInAnyOrder(100, 101)))
                .andExpect(jsonPath("$.listTagDto.[*].name", containsInAnyOrder("db_architecture", "Room")))
                .andExpect(jsonPath("$.listTagDto.[*].description", containsInAnyOrder("my sql database architecture", "Room Android best practises")))
                .andExpect(jsonPath("$.listCommentsDto.[*].id", containsInAnyOrder(100, 101)))
                .andExpect(jsonPath("$.listCommentsDto.[*].comment", containsInAnyOrder("aaa", "bbb")))
                .andExpect(jsonPath("$.isUserVote", is("DOWN_VOTE")));
    }
}