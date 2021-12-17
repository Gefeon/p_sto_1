package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository(value = "AllQuestions")
public class AllQuestionDtoDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;
    private final TagService tagService;

    public AllQuestionDtoDaoImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedIds = ((List<Long>) param.get("trackedIds"));
        Stream<QuestionDto> resultStream = entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto(q.id, q.title, q.user.id," +
                                " q.user.fullName, q.user.imageLink, count(r.count), q.description, q.persistDateTime," +
                                " q.lastUpdateDateTime, SUM(0), COUNT(answer.id)," +
                                "((Select count(up.vote) from VoteQuestion up where up.vote = 'UP_VOTE' and up.user.id = q.user.id) - " +
                                "(Select count(down.vote) from VoteQuestion down where down.vote = 'DOWN_VOTE' and down.user.id = q.user.id)))" +
                                " FROM Question q LEFT JOIN q.tags t LEFT JOIN Answer answer ON q.user.id = answer.user.id" +
                                " LEFT JOIN TrackedTag tr ON tr.trackedTag.id = t.id LEFT JOIN Reputation r ON q.user.id = r.author.id" +
                                " WHERE tr.id IN :trackedIds" +
                                " GROUP BY q.id, q.user.fullName, q.user.imageLink ORDER BY q.id", QuestionDto.class)
                .setParameter("trackedIds", trackedIds)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultStream();

        List<QuestionDto> questionDtos = resultStream
                .map(questionDto -> {
                    List<TagDto> tags = entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto" +
                            "(tag.id, tag.name, tag.description) FROM Question q JOIN Tag tag " +
                            "WHERE q.id = :questionId", TagDto.class)
                            .setParameter("questionId", questionDto.getId()).getResultList();
                    questionDto.setListTagDto(tags);
                    return questionDto;
                })
                .collect(Collectors.toList());
        return questionDtos;
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}

