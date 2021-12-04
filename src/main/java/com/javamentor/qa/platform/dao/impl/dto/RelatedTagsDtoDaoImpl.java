package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.RelatedTagsDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RelatedTagsDtoDaoImpl implements RelatedTagsDtoDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public List<RelatedTagsDto> getRelatedTagsDto() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) userDetailsService.loadUserByUsername(email);
        return em.createQuery
                ("select new com.javamentor.qa.platform.models.dto.RelatedTagsDto" +
                                "(t.id, t.name, sum(t.questions.size)) " +
                                "from Tag t inner join Question q on t.id = q.id " +
                                "where q.user.id=:ID group by t.id " +
                                "order by sum(t.questions.size) desc", RelatedTagsDto.class)
                .setParameter("ID", user.getId()).setMaxResults(10).getResultList();
    }
}
