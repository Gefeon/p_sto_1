package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository(value = "AllQuestions")
public class AllQuestionDtoDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        Stream<Tuple> resultStream = entityManager.createQuery(
"SELECT q.id as question_id, q.title as question_title, q.user.id as question_author_id, q.user.fullName as question_author_name, q.user.imageLink as question_author_image, q.description as question_description, SUM(0) as question_view_count, SUM(0) as question_count_answer, SUM(0) as question_count_valuable, t.id as tag_id, t.name as tag_name, t.description as tag_description FROM Question q LEFT JOIN q.tags t group by question_id, q.user.fullName, q.user.imageLink, t.id order by q.id", Tuple.class)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultStream();

        Map<Long, QuestionDto> questionDtoMap = new LinkedHashMap<>();

        List<QuestionDto> questionDtos = resultStream
                .map(tuple -> {
                    QuestionDto questionDto = questionDtoMap
                            .computeIfAbsent(tuple
                                            .get("question_id", Long.class),
                            id -> new QuestionDto(
                                    tuple.get("question_id", Long.class),
                                    tuple.get("question_title", String.class),
                                    tuple.get("question_author_id", Long.class),
                                    tuple.get("question_author_name", String.class),
                                    tuple.get("question_author_image", String.class),
                                    tuple.get("question_description", String.class),
                                    tuple.get("question_view_count", Long.class),
                                    tuple.get("question_count_answer", Long.class),
                                    tuple.get("question_count_valuable", Long.class)
                            )
                    );
                    if (tuple.get("tag_id") != null) {
                        TagDto tagDto = new TagDto(
                                tuple.get("tag_id", Long.class),
                                tuple.get("tag_name", String.class),
                                tuple.get("tag_description", String.class));
                        if (!questionDto.getListTagDto().contains(tagDto))
                            questionDto.getListTagDto().add(tagDto);
                    }
                    return questionDto;
                })
                .distinct()
                .collect(Collectors.toList());
        return questionDtos;
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}
//                        " q.persistDateTime as question_persist_date, q.lastUpdateDateTime as question_last_update_date," +