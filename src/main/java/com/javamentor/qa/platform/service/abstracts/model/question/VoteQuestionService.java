package com.javamentor.qa.platform.service.abstracts.model.question;

import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface VoteQuestionService {
    Optional<Long> voteAndGetSumOfVotes(Long id, VoteType type, User user);
}
