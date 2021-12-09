package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteAnswerService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    public VoteAnswerServiceImpl(VoteAnswerDao voteAnswerDao) {
        super(voteAnswerDao);
    }
}
