package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.question.TagDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TagDaoImpl extends ReadWriteDaoImpl<Tag, Long> implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean existsByName(String name) {
         long count = (long) entityManager.createQuery("SELECT COUNT(t) FROM Tag t WHERE t.name =: name").setParameter("name", name).getSingleResult();
         return count > 0;
    }

    @SuppressWarnings("unchecked") //because row use of Query is bad practice
    public Optional<Tag> getByName(String name) {
        String hql = "FROM Tag t WHERE  t.name= :name";
        TypedQuery<Tag> query = (TypedQuery<Tag>) entityManager.createQuery(hql).setParameter("name", name);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

}
