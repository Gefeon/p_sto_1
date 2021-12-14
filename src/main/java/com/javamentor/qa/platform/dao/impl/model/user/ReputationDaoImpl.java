package com.javamentor.qa.platform.dao.impl.model.user;

import com.javamentor.qa.platform.dao.abstracts.model.user.ReputationDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reputation> findByAnswerAndSender(Long answerId, Long userId) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "FROM Reputation r WHERE r.answer.id=:answerId AND r.sender.id=:userId", Reputation.class)
                .setParameter("answerId", answerId)
                .setParameter("userId", userId));
    }
}
