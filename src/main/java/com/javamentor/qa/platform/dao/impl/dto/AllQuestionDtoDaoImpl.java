package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository(value = "AllQuestions")
public class AllQuestionDtoDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedIds = ((List<Long>) param.get("trackedIds"));
        List<QuestionDto> questionDtos = entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto(q.id, q.title, q.user.id," +
                                " q.user.fullName, q.user.imageLink, count(r.count), q.description, q.persistDateTime," +
                                " q.lastUpdateDateTime, SUM(0), COUNT(answer.id)," +
                                "SUM(0))" +
                                " FROM Question q LEFT JOIN q.tags t LEFT JOIN Answer answer ON q.user.id = answer.user.id" +
                                " LEFT JOIN Reputation r ON q.user.id = r.author.id" +
                                " WHERE t.id IN :trackedIds" +
                                " GROUP BY q.id, q.user.fullName, q.user.imageLink ORDER BY q.id", QuestionDto.class)
                .setParameter("trackedIds", trackedIds)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
        List<Long> questionIds = questionDtos.stream()
                .map(QuestionDto::getId)
                .collect(Collectors.toList());

        Stream<Tuple> tags = entityManager.createQuery(
                        "SELECT t.id as tag_id, t.name as tag_name, t.description as tag_description," +
                                " q.id as question_id From Tag t JOIN t.questions q WHERE q.id in :ids order by q.id", Tuple.class)
                .setParameter("ids", questionIds)
                .getResultStream();

        Map<Long, List<TagDto>> tagsMap = new HashMap<>();
        tags.forEach(tuple -> {
            tagsMap.computeIfAbsent(tuple.get("question_id", Long.class), id -> new ArrayList<>())
                    .add(new TagDto(tuple.get("tag_id", Long.class), tuple.get("tag_name", String.class), tuple.get("tag_description", String.class)));
        });

        for (QuestionDto questionDto: questionDtos){
            questionDto.setListTagDto(tagsMap.get(questionDto.getId()));
        }

        return questionDtos;
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}

//((Select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.user.id = q.user.id) - " +
//        "(Select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.user.id = q.user.id))