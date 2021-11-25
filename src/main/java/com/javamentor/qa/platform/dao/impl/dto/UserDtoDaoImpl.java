package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;


@Repository
public class UserDtoDaoImpl implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDto> getUserDtoById(Long id) {
        try {
            return Optional.of(entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.UserDto" +
                           "(u.id, u.email, u.fullName, u.imageLink, u.city, SUM(r.count )) " +
                           "FROM User u LEFT JOIN Reputation r ON u.id = r.author.id WHERE u.id =:id GROUP BY u.id",
                           UserDto.class)
                           .setParameter("id", id)
                           .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
