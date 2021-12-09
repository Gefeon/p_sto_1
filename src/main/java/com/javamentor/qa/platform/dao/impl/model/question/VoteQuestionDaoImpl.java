package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.VoteQuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VoteQuestionDaoImpl implements VoteQuestionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Long> getSumOfVotes(Long questionId) {
        return SingleResultUtil.getSingleResultOrNull(em.createQuery(
                        "select count(v.id) from VoteQuestion v " +
                                "where v.question.id=:ID", Long.class)
                .setParameter("ID", questionId));
    }

    @Override
    public Question getQuestion(Long questionId) {
        return em.createQuery("select q from Question q where q.id=:ID", Question.class)
                .setParameter("ID", questionId)
                .getSingleResult();
    }

    @Override
    public Optional<VoteQuestion> getVoteQuestion(Long questionId, Long userId) {
        return SingleResultUtil.getSingleResultOrNull(em.createQuery(
                        "select v from VoteQuestion v inner join Question q " +
                                "on v.question.id=q.id where q.id=:qID and v.user.id=:vID", VoteQuestion.class)
                .setParameter("qID", questionId)
                .setParameter("vID", userId));
    }

    @Override
    public void saveVoteQuestion(VoteQuestion voteQuestion) {
        em.persist(voteQuestion);
    }

    @Override
    public void updateReputation(int reputationCount, Long questionId) {
        em.createQuery("update Reputation r set r.count = r.count + :COUNT where r.question.id = :ID")
                .setParameter("COUNT", reputationCount)
                .setParameter("ID", questionId)
                .executeUpdate();
    }

}
