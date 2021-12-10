package com.javamentor.qa.platform.api.controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.javamentor.qa.platform.api.abstracts.AbstractTestApi;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestAnswerResourceController extends AbstractTestApi {

    private final String url = "/api/user/question/100/answer/100";

    private static final String USER_ENTITY = "dataset/answerResourceController/user.yml";
    private static final String ROLE_ENTITY = "dataset/answerResourceController/role.yml";
    private static final String ANSWER_ENTITY = "dataset/answerResourceController/answer.yml";
    private static final String ANOTHER_ANSWER_ENTITY = "dataset/answerResourceController/anotherAnswer.yml";
    private static final String AUTH_URI = "/api/auth/token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Test
    @DataSet(value = {ANOTHER_ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {ANOTHER_ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY})
    public void deleteAnswerWithIncorrectId_returnBadRequest() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        ResultActions response = mvc.perform(delete(url).header(AUTH_HEADER, PREFIX + token.getToken()));
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {ANSWER_ENTITY, USER_ENTITY, ROLE_ENTITY}, disableConstraints = true)
    @ExpectedDataSet(value = {USER_ENTITY, ROLE_ENTITY})
    public void deleteAnswer_returnStatusOk_AnswerDeleted() throws Exception {
        AuthenticationRequestDto authDto = new AuthenticationRequestDto("user100@user.ru", "user");
        TokenResponseDto token = objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);
        ResultActions response = mvc.perform(delete(url).header(AUTH_HEADER, PREFIX + token.getToken()));
        response.andExpect(status().isOk());
    }

    //==================================================================================================================
    private final String URL_ANSWER = "/api/user/question/100/answer";
    private static final String VOTE_USER_ENTITY = "dataset/answerResourceController/vote/user_entity.yml";
    private static final String VOTE_ROLE = "dataset/answerResourceController/vote/role.yml";
    private static final String VOTE_ANSWER = "dataset/answerResourceController/vote/answer.yml";

    @Test
    @DataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/votes_on_answers.yml",
            "dataset/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/reputation.yml"
    }, disableConstraints = true)
    @ExpectedDataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/expected/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/reputation.yml",
            "dataset/expected/answerResourceController/vote/shouldVoteForUnvotedAnswerByThisUser/votes_on_answers.yml"})
    public void shouldVoteForUnvotedAnswerByThisUser() throws Exception {
        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization",
                PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mvc.perform(post(URL_ANSWER + "/101/downVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldNotVoteTheSameTypeOnAnswerByThisUser/reputation.yml",
            "dataset/answerResourceController/vote/shouldNotVoteTheSameTypeOnAnswerByThisUser/votes_on_answers.yml"
    }, disableConstraints = true)
    @ExpectedDataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldNotVoteTheSameTypeOnAnswerByThisUser/reputation.yml",
            "dataset/answerResourceController/vote/shouldNotVoteTheSameTypeOnAnswerByThisUser/votes_on_answers.yml"})
    public void shouldNotVoteTheSameTypeOnAnswerByThisUser() throws Exception {
        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isBadRequest());

        mvc.perform(post(URL_ANSWER + "/101/downVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/answerResourceController/vote/shouldVoteAnotherTypeOnAnswerByThisUser/reputation.yml",
            "dataset/answerResourceController/vote/shouldVoteAnotherTypeOnAnswerByThisUser/votes_on_answers.yml"
    }, disableConstraints = true)
    @ExpectedDataSet(value = {VOTE_USER_ENTITY, VOTE_ROLE, VOTE_ANSWER,
            "dataset/expected/answerResourceController/vote/shouldVoteAnotherTypeOnAnswerByThisUser/reputation.yml",
            "dataset/expected/answerResourceController/vote/shouldVoteAnotherTypeOnAnswerByThisUser/votes_on_answers.yml"})
    public void shouldVoteAnotherTypeOnAnswerByThisUser() throws Exception {
        mvc.perform(post(URL_ANSWER + "/100/downVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mvc.perform(post(URL_ANSWER + "/101/upVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
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
                .andExpect(status().isBadRequest());
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
        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization",
                        PREFIX + getToken("user101@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization",
                        PREFIX + getToken("user102@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
        //downVote
        mvc.perform(post(URL_ANSWER + "/100/downVote").header("Authorization",
                        PREFIX + getToken("user100@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
        mvc.perform(post(URL_ANSWER + "/100/downVote").header("Authorization",
                        PREFIX + getToken("user101@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
        mvc.perform(post(URL_ANSWER + "/100/downVote").header("Authorization",
                        PREFIX + getToken("user102@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
        //upVote
        mvc.perform(post(URL_ANSWER + "/100/upVote").header("Authorization",
                        PREFIX + getToken("user101@user.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    private String getToken(String email, String password) throws Exception {
        AuthenticationRequestDto authDtoAdmin = new AuthenticationRequestDto(email, password);
        return objectMapper.readValue(mvc
                .perform(post(AUTH_URI).content(objectMapper.writeValueAsString(authDtoAdmin)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class).getToken();
    }
}