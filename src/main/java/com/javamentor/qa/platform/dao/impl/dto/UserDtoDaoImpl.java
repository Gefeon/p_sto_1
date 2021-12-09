package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDtoDaoImpl implements UserDtoDao, PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDto> getUserDtoById(Long id) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                    "SELECT new com.javamentor.qa.platform.models.dto.UserDto" +
                       "(u.id, u.email, u.fullName, u.imageLink, u.city, SUM(r.count )) " +
                       "FROM User u LEFT JOIN Reputation r ON u.id = r.author.id WHERE u.id =:id GROUP BY u.id",
                       UserDto.class)
                    .setParameter("id", id));
    }

    //ToDo реализовать методы для пагинации
    @Override
    public List<UserDto> getItems(Map<Object, Object> param) {
        return null;
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return 0;
    }
}
