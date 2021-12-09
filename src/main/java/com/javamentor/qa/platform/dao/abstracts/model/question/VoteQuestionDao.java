package com.javamentor.qa.platform.dao.abstracts.model.question;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

import java.util.Optional;

public interface VoteQuestionDao {
    Optional<Long> getSumOfVotes(Long questionId);
    Question getQuestion(Long questionId);
    Optional<VoteQuestion> getVoteQuestion(Long questionId, Long userId);
    void saveVoteQuestion(VoteQuestion voteQuestion);
    void updateReputation(int reputationCount, Long questionId);
}
