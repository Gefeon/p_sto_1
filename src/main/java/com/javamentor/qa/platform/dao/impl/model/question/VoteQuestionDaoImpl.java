package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.VoteQuestionDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VoteQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion, Long> implements VoteQuestionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long getCountVoteQuestionByQuestionId(Long questionId) {
        return em.createQuery(
                        "select count(v.id) from VoteQuestion v " +
                                "where v.question.id=:ID", Long.class)
                .setParameter("ID", questionId).getSingleResult();
    }

    @Override
    public Question getQuestionByIdWithAuthor(Long questionId) {
        return em.createQuery("select q from Question q " +
                        "inner join User u on q.user.id = u.id " +
                        "where q.id=:ID", Question.class)
                .setParameter("ID", questionId)
                .getSingleResult();
    }

    @Override
    public Optional<VoteQuestion> getVoteQuestionByQuestionIdAndUserId(Long questionId, Long userId) {
        return SingleResultUtil.getSingleResultOrNull(em.createQuery(
                        "select v from VoteQuestion v inner join Question q " +
                                "on v.question.id=q.id where q.id=:qID and v.user.id=:vID", VoteQuestion.class)
                .setParameter("qID", questionId)
                .setParameter("vID", userId));
    }
}
