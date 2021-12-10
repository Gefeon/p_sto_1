package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.dao.abstracts.model.question.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Long> getReputationCount(Answer answer) {
        return SingleResultUtil.getSingleResultOrNull(entityManager
                .createQuery("SELECT count(*) from VoteAnswer v where v.answer=:answer", Long.class)
                .setParameter("answer", answer));
    }

    @Override
    public Optional<VoteAnswer> findByAnswerAndUser(Answer answer, User user) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "FROM VoteAnswer v where v.answer=:answer and v.user=:user", VoteAnswer.class)
                .setParameter("answer", answer)
                .setParameter("user", user));
    }
}
