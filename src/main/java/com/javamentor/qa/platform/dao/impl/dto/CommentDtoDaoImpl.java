package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
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
public class CommentDtoDaoImpl implements CommentDtoDao {

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
    @Override
    public Map<Long, List<CommentDto>> getMapCommentsByQuestionIds(List<Long> questionIds) {
        List<Tuple> comments = entityManager.createQuery
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
                                "GROUP BY c.id, c.user.fullName, q.id", Tuple.class)
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
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
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
                        "SUM(r.count)) " +
                        "FROM Comment comq " +
                        "LEFT JOIN Question q ON q.id = comq.id " +
                        "LEFT JOIN Reputation r ON comq.user.id = r.author.id " +
                        "WHERE comq.id =:id " +
                        "GROUP BY comq.id, q.id, comq.user.imageLink ",
                QuestionCommentDto.class).setParameter("id", id).getResultList();
    }
}
