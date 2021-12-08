package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
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
    public Optional<Long> voteAndGetSumOfVotes(Long id, VoteType type) {
        return voteQuestionDao.voteAndGetSumOfVotes(id, type);
    }
}
