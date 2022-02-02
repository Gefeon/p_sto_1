package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.IgnoredTagDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class IgnoredTagDaoImpl extends ReadWriteDaoImpl<IgnoredTag, Long> implements IgnoredTagDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<IgnoredTag> getIgnoredTagByName(Long id, String name) {
        return (Optional<IgnoredTag>) em.unwrap(Session.class)
                .createQuery("SELECT tr " +
                        "FROM IgnoredTag tr " +
                        "INNER JOIN Tag tag on tag.name=tr.ignoredTag.name " +
                        "INNER JOIN User u on u.id=tr.user.id " +
                        "WHERE u.id=:id and  tag.name=:name")
                .setParameter("id", id)
                .setParameter("name", name)
                .uniqueResultOptional();
    }
}
