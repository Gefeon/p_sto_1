package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TagDto> getIgnoredTagsByUserId(Long userId) {
        return em.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name, tag.description)" +
                        "FROM IgnoredTag ign INNER JOIN ign.user LEFT JOIN ign.ignoredTag tag WHERE ign.user.id = :userId", TagDto.class)
                .setParameter("userId", userId).getResultList();
    }

    @Override
    public List<TagDto> getTrackedTagsByUserId(Long userId) {
        return em.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name, tag.description)" +
                        "FROM TrackedTag tracked INNER JOIN tracked.user LEFT JOIN tracked.trackedTag tag WHERE tracked.user.id = :userId", TagDto.class)
                .setParameter("userId", userId).getResultList();
    }

    @Override
    public List<RelatedTagsDto> getRelatedTagsDto() {
        return em.createQuery
                        ("select new com.javamentor.qa.platform.models.dto.RelatedTagsDto" +
                                "(t.id, t.name, sum(t.questions.size)) " +
                                "from Tag t inner join Question q on t.id = q.id " +
                                "group by t.id " +
                                "order by sum(t.questions.size) desc", RelatedTagsDto.class)
                .setMaxResults(10).getResultList();
    }

    @Override
    public List<TagDto> getTagsByLetters(String letters) {
        return em
                .createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name, tag.description)" +
                        "FROM Tag tag WHERE tag.name LIKE :letters", TagDto.class)
                .setParameter("letters", MatchMode.ANYWHERE.toMatchString(letters))
                .setMaxResults(6).getResultList();
    }

    @Override
    public List<TagDto> getTagDtoListByQuestionId(Long id) {
        return em.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto" +
                "(tag.id, tag.name, tag.description)" +
                "FROM Tag tag where :id IN (select tt.id from tag.questions tt)", TagDto.class).setParameter("id", id).getResultList();
    }
}

