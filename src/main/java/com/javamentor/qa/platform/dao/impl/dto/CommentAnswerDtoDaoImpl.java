package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentAnswerDtoDaoImpl implements CommentAnswerDtoDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<CommentDto> getAnswerCommentDtoById(Long id) {
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.CommentDto " +
                        "(coma.id," +
                        "coma.text," +
                        "coma.user.id," +
                        "coma.user.fullName," +
                        "SUM(r.count)," +
                        "coma.persistDateTime) " +
                        "FROM Comment coma " +
                        "LEFT JOIN CommentAnswer ca ON ca.comment.id = coma.id " +
                        "LEFT JOIN Answer ans ON ans.id = ca.answer.id " +
                        "LEFT JOIN Reputation r ON coma.user.id = r.author.id " +
                        "WHERE ans.id =:id " +
                        "GROUP BY coma.id, coma.user.fullName",
                CommentDto.class).setParameter("id", id).getResultList();
    }
}
