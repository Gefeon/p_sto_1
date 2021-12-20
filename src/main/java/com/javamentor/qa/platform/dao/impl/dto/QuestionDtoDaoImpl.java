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
    public QuestionDto getQuestionDtoById(long id) {
        QuestionDto answer = null;
        Optional<QuestionDto> sss = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                                "(q.id, " +
                                "q.title, " +
                                "q.user.id , " +
                                "count(r.count), " +
                                "q.user.fullName, " +
                                "q.user.imageLink, " +
                                "q.description, " +
                                "SUM(0) , " +
                                "COUNT(answer.id), " +
                                "(Select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.user.id = q.user.id) -" +
                                "(Select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.user.id = q.user.id), " +
                                "q.persistDateTime, " +
                                "q.lastUpdateDateTime) " +
                                "FROM Question q " +
                                "LEFT JOIN Reputation r ON q.user.id = r.author.id " +
                                "LEFT JOIN Answer answer ON q.user.id = answer.user.id " +
                                "WHERE q.id =:id " +
                                "GROUP BY q.id, q.user.fullName, q.user.imageLink",
                        QuestionDto.class)
                .setParameter("id", id));
        return  sss.isEmpty()? answer : sss.get();
    }
}
