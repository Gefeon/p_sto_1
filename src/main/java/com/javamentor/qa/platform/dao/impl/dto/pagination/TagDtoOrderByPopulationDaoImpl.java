package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("TagPaginationOrderByPopulation")
public class TagDtoOrderByPopulationDaoImpl implements PageDtoDao<TagDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");

        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto" +
                        "(t.id , t.name, t.description)" +
                        " FROM Tag t JOIN t.questions q" +
                        " WHERE (q.isDeleted = :isDel)" +
                        " GROUP BY t.id, t.name, t.description" +
                        " ORDER BY COUNT(t.id) DESC", TagDto.class)
                .setParameter("isDel", false).setFirstResult((curPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage).getResultList();
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT COUNT(DISTINCT t.id) FROM Tag t" +
                        " JOIN t.questions q" +
                        " WHERE q.isDeleted = :isDel")
                .setParameter("isDel", false).getSingleResult();
    }

}
