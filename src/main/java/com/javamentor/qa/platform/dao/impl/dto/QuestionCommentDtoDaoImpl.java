package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionCommentDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionCommentDtoDaoImpl implements QuestionCommentDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionCommentDto> getQuestionCommentDtoById(Long id) {
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.QuestionCommentDto" +
                        "(comq.id, " +
                        "q.id, " +
                        "comq.lastUpdateDateTime, " +
                        "comq.persistDateTime, " +
                        "comq.text, " +
                        "comq.user.id, " +
                        "comq.user.imageLink, " +
                        "count(r.count)) " +
                        "FROM Comment comq " +
                        "LEFT JOIN Question q ON q.id = comq.id " +
                        "LEFT JOIN Reputation r ON comq.user.id = r.author.id " +
                        "WHERE comq.id =:id " +
                        "GROUP BY comq.id, q.id, comq.user.imageLink ",
                QuestionCommentDto.class).setParameter("id", id).getResultList();
    }
}
