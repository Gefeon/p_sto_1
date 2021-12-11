package com.javamentor.qa.platform.dao.abstracts.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

import java.util.Optional;

public interface VoteQuestionDao extends ReadWriteDao<VoteQuestion, Long> {
    Long getSumOfVotes(Long questionId);
    Question getQuestionByIdWithAuthor(Long questionId);
    Optional<VoteQuestion> getVoteQuestionByQuestionIdAndUserId(Long questionId, Long userId);
    void saveReputation(Reputation reputation);
}
