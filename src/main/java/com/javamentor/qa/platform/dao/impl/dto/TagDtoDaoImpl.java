package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTagDtoList() {
        List<TagDto> tagDto = null;
        tagDto = entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto" +
                "(tag.id, tag.name, tag.description )" +
                "FROM Tag tag", TagDto.class).getResultList();

        return tagDto;
    }
}
