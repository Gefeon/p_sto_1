package com.javamentor.qa.platform.dao.impl.model.user;

import com.javamentor.qa.platform.dao.abstracts.model.user.UserDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "user-email", key = "#email")
    public Optional<User> findByEmail(String email) {
        String hql = "FROM User u JOIN FETCH u.role WHERE u.email = :email";
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createQuery(hql).setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void changePassword(String email, String password) {
        String hql = "update User set password = :passwordParam where email = :email";
        entityManager.createQuery(hql)
                .setParameter("passwordParam", password)
                .setParameter("email", email)
                .executeUpdate();
    }

    public void update(String email){
        String hql = "update User set isEnabled = :isEnabled where email = :email";
        entityManager.createQuery(hql)
                .setParameter("isEnabled", false)
                .setParameter("email", email)
                .executeUpdate();
    }

}
