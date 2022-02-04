package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.TrackedTagDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<TrackedTag> getTrackedTagByUser(Long userId, Long tagId) {
        return (Optional<TrackedTag>) em.unwrap(Session.class)
                .createQuery("SELECT tag " +
                        "FROM TrackedTag tr " +
                        "INNER JOIN Tag tag on tag.id=tr.trackedTag.id " +
                        "INNER JOIN User u on u.id=tr.user.id " +
                        "WHERE u.id=:id and  tag.id=:id")
                .setParameter("id", userId)
                .setParameter("id", tagId)
                .uniqueResultOptional();
    }
}
