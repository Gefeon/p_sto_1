package com.javamentor.qa.platform.service.abstracts.model.question;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long> {
    Long voteAndGetSumOfVotes(Long questionId, VoteType type, User user);
}
