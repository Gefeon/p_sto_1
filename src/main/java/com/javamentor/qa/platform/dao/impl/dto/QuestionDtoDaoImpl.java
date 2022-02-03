package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<QuestionDto> getQuestionDtoByIdAndUserAuthId(long id, long userId) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                                "(q.id, " +
                                "q.title, " +
                                "q.user.id , " +
                                "q.user.fullName, " +
                                "q.user.imageLink, " +
                                "q.description, " +
                                "SUM(0), " +
                                "COUNT(answer.id), " +
                                "(SELECT COUNT(up.vote) FROM VoteQuestion up WHERE up.vote = 'UP_VOTE' AND up.user.id = q.user.id) -" +
                                "(SELECT COUNT(down.vote) FROM VoteQuestion down WHERE down.vote = 'DOWN_VOTE' AND down.user.id = q.user.id), " +
                                "COUNT(r.count), " +
                                "q.persistDateTime, " +
                                "q.lastUpdateDateTime," +
                                "(SELECT v.vote FROM VoteQuestion v WHERE v.question.id = q.id AND v.user.id = :userId)) " +
                                "FROM Question q " +
                                "LEFT JOIN Reputation r ON q.user.id = r.author.id " +
                                "LEFT JOIN Answer answer ON q.user.id = answer.user.id " +
                                "WHERE q.id =:id " +
                                "GROUP BY q.id, q.user.fullName, q.user.imageLink",
                        QuestionDto.class)
                .setParameter("id", id)
                .setParameter("userId", userId));
    }
}
