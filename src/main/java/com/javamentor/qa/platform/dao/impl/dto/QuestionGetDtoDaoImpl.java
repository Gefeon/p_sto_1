package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionGetDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionGetDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionGetDtoDaoImpl implements QuestionGetDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public QuestionGetDto getQuestionDtoById(long id) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionGetDto" +
                                "(q.id, " +
                                "q.title, " +
                                "q.user.id as authorId, " +
                                "count(r.count) as authorReputation, " +
                                "q.user.fullName as authorName, " +
                                "q.user.imageLink as authorImage, " +
                                "q.description, " +
                                "SUM(0) as viewCount, " +
                                "COUNT(answer.id) as countAnswer, " +
                                "(Select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.user.id = q.user.id) - " +
                                "(Select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.user.id = q.user.id) as resultVote, " +
                                "q.persistDateTime, " +
                                "q.lastUpdateDateTime as lastUpdateDateTime) " +
                                "FROM Question q " +
                                "LEFT JOIN Reputation r ON q.user.id = r.author.id " +
                                "LEFT JOIN Answer answer ON q.user.id = answer.user.id " +
                                "WHERE q.id =:id " +
                                "GROUP BY q.id, q.user.fullName, q.user.imageLink",
                        QuestionGetDto.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
