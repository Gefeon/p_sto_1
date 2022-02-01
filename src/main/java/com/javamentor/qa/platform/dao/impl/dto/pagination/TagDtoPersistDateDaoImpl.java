package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagViewDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository("TagPaginationByDate")
public class TagDtoPersistDateDaoImpl implements PageDtoDao<TagViewDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<TagViewDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        LocalDateTime localDateTime = LocalDateTime.now();
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagViewDto" +
                        "(t.id, t.name, t.description, count(DISTINCT qa.id) , count(DISTINCT qd.id), count(DISTINCT qw.id)) FROM Tag t " +
                        "LEFT JOIN t.questions qa ON qa.isDeleted = false " +
                        "LEFT JOIN t.questions qd ON qd.persistDateTime <= :now " +
                        "and :oneDay < qd.persistDateTime and qd.isDeleted = false " +
                        "LEFT JOIN t.questions qw ON qw.persistDateTime <= :now " +
                        "and :oneWeek < qw.persistDateTime and qw.isDeleted = false " +
                        "GROUP BY t " +
                        "ORDER BY t.persistDateTime DESC", TagViewDto.class)
                .setParameter("oneDay", localDateTime.minusDays(1))
                .setParameter("now", localDateTime)
                .setParameter("oneWeek", localDateTime.minusDays(7))
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT COUNT(id) FROM Tag").getSingleResult();
    }
}
