package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionDtoResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository(value = "AllQuestions")
public class AllQuestionDtoDaoImpl implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<QuestionDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        return entityManager.createQuery("SELECT " +
                        "q.id as question_id, q.title as question_title, q.user.id as question_author_id, q.user.fullName as question_author_name, q.user.imageLink as question_author_image, q.description as question_description, SUM(0) as question_view_count, SUM(0) as question_count_answer, " +
                        " SUM(0) as question_count_valuable," +
//                        " q.persistDateTime as question_persist_date, q.lastUpdateDateTime as question_last_update_date," +
                        " t.id as tag_id, t.name as tag_name, t.description as tag_description " +
                        "FROM Question q LEFT JOIN q.tags t order by q.id")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionDtoResultTransformer())
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM Question").getSingleResult();
    }
}
