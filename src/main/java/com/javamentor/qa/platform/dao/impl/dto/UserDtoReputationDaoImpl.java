package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Repository(value = "reputationDto")
public class UserDtoReputationDaoImpl implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<Object, Object> param) {
        List<UserDto> list =
                entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.UserDto" +
                "(u.id, u.email, u.fullName, u.imageLink, u.city, SUM(r.count )) " +
                "FROM User u LEFT JOIN Reputation r ON u.id = r.author.id GROUP BY u.id", UserDto.class)
                .getResultList();
        list.sort(Comparator.comparing(UserDto::getReputation, Comparator.reverseOrder()));
        return list;
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT count (id) FROM User").getSingleResult();
    }
}
