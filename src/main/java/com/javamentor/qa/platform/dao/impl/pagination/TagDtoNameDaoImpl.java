package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository(value = "TagPaginationByName")
public class TagDtoNameDaoImpl implements PageDtoDao<TagDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getItems(Map<Object, Object> param) {

        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");

        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto" +
                                            "(t.id , t.name, t.description )" +
                                            "FROM Tag t ORDER BY t.name", TagDto.class)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT COUNT(id) FROM Tag ").getSingleResult();
    }
}
