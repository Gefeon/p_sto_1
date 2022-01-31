package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository(value = "AllQuestions")
public class QuestionDtoAllDataDaoImpl implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionViewDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedIds = ((List<Long>) param.get("trackedIds"));
        List<Long> ignoredIds = ((List<Long>) param.get("ignoredIds"));
        if (ignoredIds == null) {
            ignoredIds = new ArrayList<>();
            ignoredIds.add(-1L);
        }

        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionViewDto(q.id, q.title, q.user.id," +
                                " q.user.fullName, q.user.imageLink, q.description, SUM(0), COUNT(DISTINCT answer.id)," +
                                " ((Select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.question.id = q.id) - " +
                                " (Select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.question.id = q.id))," +
                                " SUM(r.count)/(COUNT(r.id) / nullif(COUNT(DISTINCT r.id),0)), q.persistDateTime, q.lastUpdateDateTime)" +
                                " FROM Question q JOIN q.tags t LEFT JOIN Answer answer ON q.id = answer.question.id" +
                                " LEFT JOIN Reputation r ON q.user.id = r.author.id" +
                                " WHERE q.id IN (SELECT q.id From Question q JOIN q.tags t WHERE :trackedIds IS NULL OR t.id IN :trackedIds)" +
                                " AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags t WHERE t.id IN :ignoredIds)" +
                                " GROUP BY q.id, q.user.fullName,q.user.imageLink ORDER BY q.id", QuestionViewDto.class)
                .setParameter("trackedIds", trackedIds)
                .setParameter("ignoredIds", ignoredIds)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public long getTotalResultCount(Map<Object, Object> param) {
        List<Long> trackedIds = ((List<Long>) param.get("trackedIds"));
        List<Long> ignoredIds = ((List<Long>) param.get("ignoredIds"));
        if (ignoredIds == null) {
            ignoredIds = new ArrayList<>();
            ignoredIds.add(-1L);
        }
        return (Long) entityManager.createQuery("SELECT COUNT(DISTINCT q.id) FROM Question q JOIN q.tags t" +
                        " WHERE q.id IN (SELECT q.id From Question q JOIN q.tags t WHERE :trackedIds IS NULL OR t.id IN :trackedIds)" +
                        " AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags t WHERE t.id IN :ignoredIds)")
                .setParameter("trackedIds", trackedIds)
                .setParameter("ignoredIds", ignoredIds)
                .getSingleResult();
    }
}

