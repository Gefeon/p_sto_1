package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
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

    @Override
    public List<QuestionDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        Long tagId = (Long) param.get("tagId");
        List listQuestionDto = entityManager
                .createQuery("select q.id as q_id, " +
                        "q.title as q_title, " +
                        "u.id as q_author_id, " +
                        "u.nickname as q_author_name, " +
                        "u.imageLink as q_author_image, " +
                        "q.description as q_description, " +
                        "(0L) as q_view_count, " +
                        "(select count(*) from Answer a where a.question.id = q.id) as q_count_answer, " +
                        "((select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.user.id = q.user.id) - (select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.user.id = q.user.id)) as q_count_valuable, " +
                        "(select count(r.count) from Reputation r where r.author.id = u.id) as q_author_reputation, " +
                        "q.persistDateTime as q_persist_date_time, " +
                        "q.lastUpdateDateTime as q_last_update_datetime, " +
                        "t.id as t_id, " +
                        "t.name as t_name, " +
                        "t.description as t_description " +
                        "from Question q " +
                        "join User u on q.user.id = u.id " +
                        "join q.tags as t where t.id = :tagId " +
                        "order by q.id")
                .setParameter("tagId", tagId)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionDtoTagResultTransformer())
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();

        return listQuestionDto;
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        Long tagId = (Long) param.get("tagId");
        return (Long) entityManager.createQuery("select count(q) from Question q join q.tags t where t.id = :tagId")
                .setParameter("tagId", tagId)
                .getSingleResult();
    }
}
