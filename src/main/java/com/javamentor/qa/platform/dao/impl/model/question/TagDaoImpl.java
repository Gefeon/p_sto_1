package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.TagDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl extends ReadWriteDaoImpl<Tag, Long> implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean existsByName(String name) {
        long count = (long) entityManager.createQuery("SELECT COUNT(t) FROM Tag t WHERE t.name =: name").setParameter("name", name).getSingleResult();
        return count > 0;
    }

    @SuppressWarnings("unchecked")
    public Optional<Tag> getByName(String name) {
        TypedQuery<Tag> query = (TypedQuery<Tag>) entityManager
                .createQuery("FROM Tag t WHERE  t.name= :name")
                .setParameter("name", name);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @SuppressWarnings("unchecked") //because row use of List is bad practice
    @Override
    public List<Tag> getByAllNames(Collection<String> names) {
        if (names != null && names.size() > 0) {
            return (List<Tag>) entityManager
                    .createQuery("FROM Tag t WHERE  t.name IN :names")
                    .setParameter("names", names).getResultList();
        }
        return new ArrayList<>();
    }
}

