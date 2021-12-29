package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("QuestionNoAnswer")
public class QuestionDtoNoAnswerDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionDto> getItems(Map<Object, Object> param) {

        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = ((List<Long>) param.get("trackedTags"));
        List<Long> ignoredTags = ((List<Long>) param.get("ignoredTags"));

        return entityManager.createQuery("SELECT NEW com.javamentor.qa.platform.models.dto.QuestionDto " +
                        "(q.id, " +
                        "q.title, " +
                        "q.user.id, " +
                        "q.user.fullName, " +
                        "q.user.imageLink, " +
                        "q.description, " +
                        "SUM(0), " +
                        "(SELECT COUNT(a.id) FROM Answer a WHERE a.question.id = q.id), " +
                        "((SELECT COUNT (up.vote) FROM VoteQuestion up WHERE up.vote = 'UP_VOTE' AND up.user.id = q.user.id) - " +
                        "(SELECT COUNT(down.vote) FROM VoteQuestion down WHERE down.vote = 'DOWN_VOTE' AND down.user.id = q.user.id)), " +
                        "(SELECT SUM (r.count) FROM Reputation r WHERE q.user.id = r.author.id), " +
                        "q.persistDateTime, " +
                        "q.lastUpdateDateTime) " +
                        "FROM Question q  " +
                        "JOIN q.tags tgs " +
                        "WHERE q.id IN (SELECT q.id From Question q JOIN q.tags tgs WHERE :tracked IS NULL OR tgs.id IN :tracked) " +
                        "AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs WHERE tgs.id IN :ignored) " +
                        "AND q.id NOT IN (SELECT a.question.id FROM Answer a) " +
                        "GROUP BY q.id , q.user.fullName, q.user.imageLink ORDER BY q.id ", QuestionDto.class)
                .setParameter("tracked", trackedTags)
                .setParameter("ignored", ignoredTags)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }


    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");

        return (Long) entityManager.createQuery("SELECT COUNT(DISTINCT q.id) FROM Question q JOIN q.tags tgs" +
                        " WHERE q.id NOT IN (SELECT a.question.id FROM Answer a ) AND q.id IN (SELECT q.id From Question q JOIN q.tags tgs WHERE :tracked IS NULL OR tgs.id IN :tracked)" +
                        " AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs WHERE tgs.id IN :ignored)")
                .setParameter("tracked", trackedTags)
                .setParameter("ignored", ignoredTags)
                .getSingleResult();
    }
}