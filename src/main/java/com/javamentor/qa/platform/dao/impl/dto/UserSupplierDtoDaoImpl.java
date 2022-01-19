package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserSupplierDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.UserSupplierDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserSupplierDtoDaoImpl implements UserSupplierDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserSupplierDto> getUserDtoById(Long id) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                    "SELECT new com.javamentor.qa.platform.models.dto.UserSupplierDto" +
                       "(u.id, u.email, u.fullName, u.imageLink, u.city, SUM(COALESCE(r.count, 0))) " +
                       "FROM User u LEFT JOIN Reputation r ON u.id = r.author.id WHERE u.id =:id GROUP BY u.id",
                       UserSupplierDto.class)
                    .setParameter("id", id));
    }
}
