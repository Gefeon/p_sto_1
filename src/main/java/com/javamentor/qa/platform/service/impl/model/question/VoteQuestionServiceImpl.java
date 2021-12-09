package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteQuestionServiceImpl implements VoteQuestionService {

    @Autowired
    private VoteQuestionDao voteQuestionDao;

    @Override
    @Transactional
    public Optional<Long> voteAndGetSumOfVotes(Long id, VoteType type, User user) {

        int reputationCount;
        if (type == VoteType.DOWN_VOTE) {
            reputationCount = -5;
        } else {
            reputationCount = 10;
        }

        Question question = voteQuestionDao.getQuestion(id);

        if (voteQuestionDao.getVoteQuestion(id, user.getId()).isEmpty()) {
            voteQuestionDao.saveVoteQuestion(new VoteQuestion(user, question, type));
            voteQuestionDao.updateReputation(reputationCount, id);
        }

        return voteQuestionDao.getSumOfVotes(id);
    }
}
