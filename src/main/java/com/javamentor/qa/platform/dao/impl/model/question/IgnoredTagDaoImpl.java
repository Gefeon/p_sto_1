package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.IgnoredTagDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IgnoredTagDaoImpl extends ReadWriteDaoImpl<IgnoredTag, Long> implements IgnoredTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> getTagsByUser(User user) {
        if(user != null) {
            return entityManager
                    .createQuery("SELECT ign.ignoredTag FROM IgnoredTag ign INNER JOIN ign.user LEFT JOIN ign.ignoredTag WHERE  ign.user = :user", Tag.class)
                    .setParameter("user", user).getResultList();
        }
        return new ArrayList<>();
    }
}

