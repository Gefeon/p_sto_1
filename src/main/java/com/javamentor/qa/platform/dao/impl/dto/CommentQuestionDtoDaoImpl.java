package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentQuestionDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentQuestionDtoDaoImpl implements CommentQuestionDtoDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Map<Long, List<CommentDto>> getMapCommentsByQuestionIds(List<Long> questionIds) {
        List<Tuple> comments = em.createQuery
                        ("SELECT c.id as com_id, " +
                                "c.text as com_text, " +
                                "c.user.id as com_userId, " +
                                "c.user.fullName as com_userFullName," +
                                "SUM(r.count) as com_rep," +
                                "c.persistDateTime as com_persistDateTime," +
                                "q.id as question_id " +
                                "FROM Comment c " +
                                "LEFT JOIN CommentQuestion cq ON c.id = cq.comment.id " +
                                "LEFT JOIN Question q ON cq.question.id = q.id " +
                                "LEFT JOIN Reputation r ON q.user.id = r.author.id " +
                                "WHERE q.id IN :ids " +
                                "GROUP BY c.id, c.user.fullName, q.id"
                                , Tuple.class)
                .setParameter("ids", questionIds)
                .getResultList();
        Map<Long, List<CommentDto>> commentsMap = new HashMap<>();
        comments.forEach(tuple -> commentsMap.computeIfAbsent(tuple.get("question_id", Long.class), id -> new ArrayList<>())
                .add(new CommentDto(
                        tuple.get("com_id", Long.class),
                        tuple.get("com_text", String.class),
                        tuple.get("com_userId", Long.class),
                        tuple.get("com_userFullName", String.class),
                        tuple.get("com_rep", Long.class),
                        tuple.get("com_persistDateTime", LocalDateTime.class))));
        return commentsMap;
    }

    @Override
    public List<CommentDto> getCommentDtoListByQuestionId(Long id) {
        return em.createQuery("SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                        "(comq.id, " +
                        "comq.text, " +
                        "comq.user.id, " +
                        "comq.user.fullName, " +
                        "SUM(r.count), " +
                        "comq.persistDateTime)" +
                        "FROM Comment comq " +
                        "LEFT JOIN CommentQuestion cq ON cq.comment.id = comq.id " +
                        "LEFT JOIN Question q ON q.id = cq.question.id " +
                        "LEFT JOIN Reputation r ON comq.user.id = r.author.id " +
                        "WHERE q.id =:id " +
                        "GROUP BY comq.id, comq.user.fullName ",
                CommentDto.class).setParameter("id", id).getResultList();
    }
}