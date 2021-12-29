package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.webapp.converters.transformers.QuestionDtoTagResultTransformer;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository(value = "QuestionDtoPaginationByTag")
public class QuestionDtoTagDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<QuestionDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        Long tagId = (Long) param.get("tagId");

        return entityManager
                .createQuery("SELECT q.id AS q_id, " +
                        "q.title AS q_title, " +
                        "u.id AS q_author_id, " +
                        "u.nickname AS q_author_name, " +
                        "u.imageLink AS q_author_image, " +
                        "q.description AS q_description, " +
                        "(0L) AS q_view_count, " +
                        "(SELECT COUNT(*) FROM Answer a WHERE a.question.id = q.id) AS q_count_answer, " +
                        "((SELECT COUNT(up.vote) FROM VoteQuestion up WHERE up.vote = 'UP_VOTE' AND up.user.id = q.user.id) -" +
                        "(SELECT COUNT(down.vote) FROM VoteQuestion down WHERE down.vote = 'DOWN_VOTE' AND down.user.id = q.user.id)) AS q_count_valuable, " +
                        "(SELECT COUNT(r.count) FROM Reputation r WHERE r.author.id = u.id) AS q_author_reputation, " +
                        "q.persistDateTime AS q_persist_date_time, " +
                        "q.lastUpdateDateTime AS q_last_update_datetime " +
                        "FROM Question q " +
                        "JOIN User u ON q.user.id = u.id " +
                        "JOIN q.tags AS t WHERE t.id = :tagId " +
                        "ORDER BY q.id")
                .setParameter("tagId", tagId)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionDtoTagResultTransformer())
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        Long tagId = (Long) param.get("tagId");
        return (Long) entityManager.createQuery("SELECT COUNT(q) FROM Question q JOIN q.tags t WHERE t.id = :tagId")
                .setParameter("tagId", tagId)
                .getSingleResult();
    }
}
