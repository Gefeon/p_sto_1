package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.VoteQuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VoteQuestionDaoImpl implements VoteQuestionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Long> voteAndGetSumOfVotes(Long id, VoteType type) {

        int reputationCount;

        if (type == VoteType.DOWN_VOTE) {
            reputationCount = -5;
        } else {
            reputationCount = 10;
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Question question = em.createQuery("select q from Question q where q.id=:ID", Question.class)
                .setParameter("ID", id)
                .getSingleResult();

        Optional<VoteQuestion> voteQuestion = SingleResultUtil.getSingleResultOrNull(em.createQuery(
                        "select v from VoteQuestion v inner join Question q " +
                                "on v.question.id=q.id where q.id=:qID and v.user.id=:vID", VoteQuestion.class)
                .setParameter("qID", id)
                .setParameter("vID", user.getId()));

        if (voteQuestion.isEmpty()) {
            em.persist(new VoteQuestion(user, question, type));
            em.createQuery("update Reputation r set r.count = r.count + :COUNT where r.question.id = :ID")
                    .setParameter("COUNT", reputationCount)
                    .setParameter("ID", id)
                    .executeUpdate();
        }

        return SingleResultUtil.getSingleResultOrNull(em.createQuery(
                        "select count(v.id) from VoteQuestion v " +
                                "where v.question.id=:ID", Long.class)
                .setParameter("ID", id));
    }
}
