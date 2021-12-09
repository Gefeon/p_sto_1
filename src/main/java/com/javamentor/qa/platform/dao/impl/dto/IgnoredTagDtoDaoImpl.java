package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.IgnoredTagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IgnoredTagDtoDaoImpl implements IgnoredTagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTagsByUserId(Long userId) {
        if(userId != null) {
             return entityManager
                    .createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name, tag.description)" +
                            "FROM IgnoredTag ign INNER JOIN ign.user LEFT JOIN ign.ignoredTag tag WHERE ign.user.id = :userId", TagDto.class)
                    .setParameter("userId", userId).getResultList();
        }
        return new ArrayList<>();
    }
}

