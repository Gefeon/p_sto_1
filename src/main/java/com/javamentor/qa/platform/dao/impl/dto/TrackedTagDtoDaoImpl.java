package com.javamentor.qa.platform.dao.impl.dto;


import com.javamentor.qa.platform.dao.abstracts.dto.TrackedTagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TrackedTagDtoDaoImpl implements TrackedTagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTagsByUserId(Long userId) {
        return entityManager
                .createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name, tag.description)" +
                        "FROM TrackedTag track INNER JOIN track.user LEFT JOIN track.trackedTag tag WHERE track.user.id = :userId", TagDto.class)
                .setParameter("userId", userId).getResultList();
    }
}
