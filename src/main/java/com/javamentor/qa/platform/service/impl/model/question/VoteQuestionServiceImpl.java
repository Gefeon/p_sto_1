package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.question.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteQuestionService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion, Long> implements VoteQuestionService {

    private final VoteQuestionDao voteQuestionDao;

    public VoteQuestionServiceImpl(ReadWriteDao<VoteQuestion, Long> readWriteDao, VoteQuestionDao voteQuestionDao) {
        super(readWriteDao);
        this.voteQuestionDao = voteQuestionDao;
    }

    @Override
    @Transactional
    public Long voteAndGetSumOfVotes(Long questionId, VoteType type, User user) {

        int reputationCount = 0;
        if (type == VoteType.DOWN_VOTE) reputationCount = -5;
        if (type == VoteType.UP_VOTE) reputationCount = 10;

        Question question = voteQuestionDao.getQuestionByIdWithAuthor(questionId);
        User author = question.getUser();

        if (voteQuestionDao.getVoteQuestionByQuestionIdAndUserId(questionId, user.getId()).isEmpty()) {
            voteQuestionDao.persist(new VoteQuestion(user, question, type));
            voteQuestionDao.saveReputation(new Reputation(author, user,
                    reputationCount, ReputationType.VoteQuestion, question));
        }

        return voteQuestionDao.getSumOfVotes(questionId);
    }
}
