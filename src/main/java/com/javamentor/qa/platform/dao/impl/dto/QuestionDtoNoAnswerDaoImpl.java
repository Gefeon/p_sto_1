package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("QuestionNoAnswer")
public class QuestionDtoNoAnswerDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<QuestionDto> getItems(Map<Object, Object> param) {

        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = ((List<Long>) param.get("trackedIds"));
        List<Long> ignoredTags = ((List<Long>) param.get("ignoredIds"));
        if (ignoredTags == null) {
            ignoredTags = new ArrayList<>();
            ignoredTags.add(-1L);
        }
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                        "(q.id, " +
                        "q.title, " +
                        "q.user.id, " +
                        "q.user.fullName, " +
                        "q.user.imageLink, " +
                        "q.description, " +
                        "SUM(0), " +
                        "(SELECT COUNT(a.id) FROM Answer a WHERE a.question.id = q.id), " +
                        "((Select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.user.id = q.user.id) - " +
                        "(Select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.user.id = q.user.id)), " +
                        "(SELECT SUM (r.count) FROM Reputation r WHERE q.user.id = r.author.id), " +
                        "q.persistDateTime, " +
                        "q.lastUpdateDateTime) " +
                        "FROM Question q where q.id not in (select a.question.id from Answer a ) " +
                        "GROUP BY q.id , q.user.fullName, q.user.imageLink order by q.id ", QuestionDto.class)
                .setParameter("tracked", trackedTags)
                .setParameter("ignored", ignoredTags)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }



    @Override
    public long getTotalResultCount(Map param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}
