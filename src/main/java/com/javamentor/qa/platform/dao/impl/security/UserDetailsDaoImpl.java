package com.javamentor.qa.platform.dao.impl.security;

import com.javamentor.qa.platform.dao.abstracts.security.UserDetailsDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

/**
 * @author Alexey Achkasov
 * @version 1.0, 26.11.2021
 */
@Repository
public class UserDetailsDaoImpl implements UserDetailsDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public Optional<UserDetails> loadUserByUsername(String username) {
        String hql = "FROM User u JOIN FETCH u.role WHERE email = :email";
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createQuery(hql).setParameter("email", username);
        Optional<User> user = SingleResultUtil.getSingleResultOrNull(query);
        return user.isPresent() ? Optional.of(user.get()) : Optional.empty();
    }
}