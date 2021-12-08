package com.javamentor.qa.platform.dao.abstracts.model.question;

import com.javamentor.qa.platform.models.entity.question.answer.VoteType;

import java.util.Optional;

public interface VoteQuestionDao {
    Optional<Long> voteAndGetSumOfVotes(Long id, VoteType type);
}
