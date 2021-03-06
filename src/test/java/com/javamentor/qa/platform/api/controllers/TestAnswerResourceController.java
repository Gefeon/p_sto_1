package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAnswerResourceController extends AbstractTestApi {

    private final String url = "/api/user/question/100/answer/100";

    private static final String USER_ENTITY = "dataset/answerResourceController/user.yml";
    private static final String ROLE_ENTITY = "dataset/answerResourceController/role.yml";
    private static final String ANSWER_ENTITY = "dataset/answerResourceController/answer.yml";
    private static final String ANSWER_DTO = "dataset/answerResourceController/getAnswerByQuestionId/answer.yml";
    private static final String USER_DTO = "dataset/answerResourceController/getAnswerByQuestionId/user.yml";
    private static final String ROLE_DTO = "dataset/answerResourceController/getAnswerByQuestionId/role.yml";
    private static final String QUESTION_DTO = "dataset/answerResourceController/getAnswerByQuestionId/question.yml";
    private static final String REPUTATION_DTO = "dataset/answerResourceController/getAnswerByQuestionId/reputation.yml";
    private static final String VOTE_ANSWER_DTO = "dataset/answerResourceController/getAnswerByQuestionId/vote_answer.yml";
    private static final String ANOTHER_ANSWER_ENTITY = "dataset/answerResourceController/another_answer.yml";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {ANOTHER_ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {ANOTHER_ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY})
    public void deleteAnswerWithIncorrectId_returnBadRequest() throws Exception {
        ResultActions response = mvc.perform(delete(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY,
            "dataset/answerResourceController/votes_on_answers.yml",
            "dataset/answerResourceController/reputation.yml"}, disableConstraints = true)
    @ExpectedDataSet(value = {USER_ENTITY, ROLE_ENTITY})
    public void deleteAnswer_returnStatusOk_AnswerDeleted() throws Exception {
        ResultActions response = mvc.perform(delete(url).header(AUTH_HEADER, PREFIX + getToken("user100@user.ru", "user")));
        response.andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {ANSWER_DTO, USER_DTO, ROLE_DTO, QUESTION_DTO, REPUTATION_DTO, VOTE_ANSWER_DTO}, disableConstraints = true)
    public void getAnswersByQuestionId() throws Exception {

        String token = getToken("user100@user.ru", "user");

        // ?????????????????????? ????????????
        mvc.perform(get("/api/user/question/100/answer").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionId", is(100)))
                .andExpect(jsonPath("$[0].userId", is(100)))
                .andExpect(jsonPath("$[0].userReputation", is(30)))
                .andExpect(jsonPath("$[0].countValuable", is(2)))
                .andExpect(jsonPath("$[1].userId", is(101)))
                .andExpect(jsonPath("$[1].userReputation", is(33)))
                .andExpect(jsonPath("$[1].countValuable", is(1)))
                .andExpect(jsonPath("$[2].questionId", is(100)))
                .andExpect(jsonPath("$[2].userId", is(103)))
                .andExpect(jsonPath("$[2].userReputation", is(33)))
                .andExpect(jsonPath("$[2].countValuable", is(-1)));

        // ?????????????? ?? ?????????? Id ???? ????????????????????
        mvc.perform(get("/api/user/question/1000/answer").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        // ???? ???????????? ???????????? Id
        mvc.perform(get("/api/user/question/ggg/answer").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    //==================================================================================================================
    private static final String ANSWER_COMMENTS_DTO = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/answer.yml";
    private static final String USER_COMMENTS_DTO = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/user.yml";
    private static final String ROLE_COMMENTS_DTO = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/role.yml";
    private static final String QUESTION_COMMENTS_DTO = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/question.yml";
    private static final String REPUTATION_COMMENTS_DTO = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/reputation.yml";
    private static final String VOTE_ANSWER__COMMENTS_DTO = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/vote_answer.yml";
    private static final String COMMENT_DTO = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/comment.yml";
    private static final String COMMENT_ANSWER = "dataset/answerResourceController/getCommentsDtoInAnswerByQuestionId/comment_answer.yml";

    @Test
    @DataSet(value = {ANSWER_COMMENTS_DTO, USER_COMMENTS_DTO, ROLE_COMMENTS_DTO,
            QUESTION_COMMENTS_DTO, REPUTATION_COMMENTS_DTO, VOTE_ANSWER__COMMENTS_DTO, COMMENT_DTO, COMMENT_ANSWER}, disableConstraints = true)
    public void getCommentsDtoInAnswersByQuestionId() throws Exception {

        String token = getToken("user100@user.ru", "user");

        mvc.perform(get("/api/user/question/100/answer").header(AUTH_HEADER, PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].comments[*].id", containsInRelativeOrder(101,102,103,104)))
                .andExpect(jsonPath("$[*].comments[*].comment", containsInRelativeOrder("Hello world",
                        "Hello brother", "Hello mother", "Hello wife")))
                .andExpect(jsonPath("$[*].comments[*].userId", containsInRelativeOrder(100, 101, 102, 103)))
                .andExpect(jsonPath("$[*].comments[*].fullName", containsInRelativeOrder("Ivan", "Olga", "Elena", "Roman")))
                .andExpect(jsonPath("$[*].comments[*].reputation", containsInRelativeOrder(30, 33, 22, 0)));
    }

    //==================================================================================================================
    private final String URL_ANSWER = "/api/user/question/100/answer";
    private static final String VOTE_USER_ENTITY = "dataset/answerResourceController/vote/user_entity.yml";
    private static final String VOTE_ROLE = "dataset/answerResourceController/vote/role.yml";
    private static final String VOTE_ANSWER = "dataset/answerResourceController/vote/answer.yml";

    @Test
    @DataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/reputation.yml",
            "dataset/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/votes_on_answers.yml"
    }, disableConstraints = true)
    @ExpectedDataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/expected/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/reputation.yml",
            "dataset/expected/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/votes_on_answers.yml"})
    public void shouldVoteForUnvotedAnswerByThisUser() throws Exception {
        String token100 = getToken("user100@user.ru", "user");

        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization", PREFIX + token100))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mvc.perform(post(URL_ANSWER + "/101/downVote").header("Authorization", PREFIX + token100))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldNotVoteAgainOnAnswerByThisUser/reputation.yml",
            "dataset/answerResourceController/vote/shouldNotVoteAgainOnAnswerByThisUser/votes_on_answers.yml"
    }, disableConstraints = true)
    @ExpectedDataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldNotVoteAgainOnAnswerByThisUser/reputation.yml",
            "dataset/answerResourceController/vote/shouldNotVoteAgainOnAnswerByThisUser/votes_on_answers.yml"})
    public void shouldNotVoteAgainOnAnswerByThisUser() throws Exception {
        String token100 = getToken("user100@user.ru", "user");

        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization", PREFIX + token100))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User is already voted"));

        mvc.perform(post(URL_ANSWER + "/100/downVote").header("Authorization", PREFIX + token100))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User is already voted"));
    }

    @Test
    @DataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldNotVoteOnAnswerWithNonExistentId/reputation.yml",
            "dataset/answerResourceController/vote/shouldNotVoteOnAnswerWithNonExistentId/votes_on_answers.yml"
    }, disableConstraints = true)
    @ExpectedDataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldNotVoteOnAnswerWithNonExistentId/reputation.yml",
            "dataset/answerResourceController/vote/shouldNotVoteOnAnswerWithNonExistentId/votes_on_answers.yml"})
    public void shouldNotVoteOnAnswerWithNonExistentId() throws Exception {
        mvc.perform(post(URL_ANSWER + "/999/upVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Answer with this id does not exist"));
    }

    @Test
    @DataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldBeACorrectNumberOfVotesOnAnswerAfterVoting/reputation.yml",
            "dataset/answerResourceController/vote/shouldBeACorrectNumberOfVotesOnAnswerAfterVoting/votes_on_answers.yml"
    }, disableConstraints = true)
    @ExpectedDataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/expected/answerResourceController/vote/shouldBeACorrectNumberOfVotesOnAnswerAfterVoting/reputation.yml",
            "dataset/expected/answerResourceController/vote/shouldBeACorrectNumberOfVotesOnAnswerAfterVoting/votes_on_answers.yml"})
    public void shouldBeACorrectNumberOfVotesOnAnswerAfterVoting() throws Exception {
        //upVote
        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        //downVote
        mvc.perform(post(URL_ANSWER + "/100/downVote").header("Authorization",
                        PREFIX + getToken("user101@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
        //downVote
        mvc.perform(post(URL_ANSWER + "/100/downVote").header("Authorization",
                        PREFIX + getToken("user102@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
        //upVote
        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization",
                        PREFIX + getToken("user103@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }
}