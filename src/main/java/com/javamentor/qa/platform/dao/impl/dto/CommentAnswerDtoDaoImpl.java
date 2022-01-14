package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
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
public class CommentAnswerDtoDaoImpl implements CommentAnswerDtoDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Map<Long, List<CommentDto>> getCommentsDtoByAnswersIds(List<Long> answerIds) {

        List<Tuple> comments = entityManager.createQuery(
                "SELECT " +
                        "c.id as c_id, c.text as c_text, c.user.id as user_id, " +
                        "c.user.fullName as user_fullName, " +
                        "SUM(COALESCE(r.count, 0)) as a_reputation, " +
                        "c.persistDateTime as c_per_date, " +
                        "ans.id as ans_id " +
                        "FROM Comment c " +
                        "LEFT JOIN CommentAnswer ca ON ca.comment.id = c.id " +
                        "LEFT JOIN Answer ans ON ans.id = ca.answer.id " +
                        "LEFT JOIN Reputation r ON c.user.id = r.author.id " +
                        "WHERE ans.id  IN :id " +
                        "GROUP BY c.id, c.user.fullName, ans.id",
                Tuple.class).setParameter("id", answerIds).getResultList();

        Map<Long, List<CommentDto>> commentsDtoMap = new HashMap<>();

        comments.forEach(tuple -> commentsDtoMap.computeIfAbsent(tuple.get("ans_id", Long.class), id -> new ArrayList<>())
                .add(new CommentDto(tuple.get("c_id", Long.class),
                        tuple.get("c_text", String.class),
                        tuple.get("user_id", Long.class),
                        tuple.get("user_fullName", String.class),
                        tuple.get("a_reputation", Long.class),
                        tuple.get("c_per_date", LocalDateTime.class)
                        )
                )
        );
        return commentsDtoMap;
    }
}
