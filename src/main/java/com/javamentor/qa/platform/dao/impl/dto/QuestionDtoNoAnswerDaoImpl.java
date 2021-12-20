package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.UserDto;
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
    public List<QuestionDto> getItems(Map<Object, Object> param) {

        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                        "(q.id, " +
                        "q.title, " +
                        "q.description, " +
                        "q.persistDateTime, " +
                        "q.lastUpdateDateTime, " +
                        "q.user.id," +
                        "q.user.fullName, " +
                        "q.user.imageLink, " +
                        "SUM(r.count), " +
                        "count(a.question.id), " +
                        "((Select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.user.id = q.user.id) - " +
                        "(Select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.user.id = q.user.id)))" +
                        "FROM Question q LEFT JOIN Reputation r ON q.user.id = r.author.id " +
                        "LEFT JOIN Answer a ON q.id = a.question.id where " +
                       // "LEFT JOIN Answer a ON q.id = a.question.id  " +
                        //"LEFT JOIN Answer a ON q.id = a.question.id WHERE a.question.id is null" +
                        "GROUP BY q.id , q.user.fullName, q.user.imageLink order by SUM(r.count)", QuestionDto.class)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }



    @Override
    public long getTotalResultCount(Map param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}
