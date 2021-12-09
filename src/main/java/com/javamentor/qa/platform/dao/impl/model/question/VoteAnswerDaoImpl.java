package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.dao.abstracts.model.question.VoteAnswerDao;
import org.springframework.stereotype.Repository;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {
}
