package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.RelatedTagsDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RelatedTagsDtoDaoImpl implements RelatedTagsDtoDao {

    @PersistenceContext
    private EntityManager em;

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
}
