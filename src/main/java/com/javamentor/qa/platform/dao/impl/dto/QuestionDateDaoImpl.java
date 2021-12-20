package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository(value = "QuestionByDate")
public class QuestionDateDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionDto> getItems(Map<Object, Object> param) {

        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");

        List<QuestionDto> questionDtos = entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto" +
                                "(q.id, " +
                                "q.title, " +
                                "q.user.id, " +
                                "q.user.fullName, " +
                                "q.user.imageLink, " +
                                "q.description, " +
                                "SUM(0), " +
                                "(SELECT COUNT(answer.id) FROM Answer answer WHERE answer.user.id = q.user.id), " +
                                "(SELECT COUNT (up.vote) FROM VoteQuestion up WHERE up.vote = 'UP_VOTE' AND up.user.id = q.user.id) - " +
                                "(SELECT COUNT(down.vote) FROM VoteQuestion down WHERE down.vote = 'DOWN_VOTE' AND down.user.id = q.user.id)," +
                                "(SELECT SUM (r.count) FROM Reputation r WHERE q.user.id = r.author.id), " +
                                "q.persistDateTime, " +
                                "q.lastUpdateDateTime)" +
                                "FROM Question q " +
                                "JOIN q.tags tgs " +
                                "WHERE q.id IN (SELECT q.id From Question q JOIN q.tags tgs WHERE :tracked IS NULL OR tgs.id IN :tracked)" +
                                "AND q.id NOT IN (SELECT q.id From Question q JOIN q.tags tgs WHERE tgs.id IN :ignored)" +
                                "GROUP BY q.id, q.user.fullName, q.user.imageLink ORDER BY q.persistDateTime DESC ",
                                QuestionDto.class)
                        .setParameter("ignored", ignoredTags)
                        .setParameter("tracked", trackedTags)
                        .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                        .getResultList();

        Map<Long, List<TagDto>> map = questionDtos.stream().collect(Collectors.toMap(
                q -> (Long) q.getId(),
                q ->
                        entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name, tag.description)" +
                                        "FROM Tag tag " +
                                        "JOIN tag.questions q WHERE q.id IN (:qId)" +
                                        "GROUP BY tag.id", TagDto.class)
                                .setParameter("qId", q.getId())
                                .getResultList()
        ));

        questionDtos.forEach(q -> q.setListTagDto(map.get(q.getId())));

        return questionDtos;
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}
