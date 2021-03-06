package com.javamentor.qa.platform.dao.abstracts.model.user;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface UserDao extends ReadWriteDao<User, Long> {

    Optional<User> findByEmail(String email);
    void changePassword(String email, String password);

    void changeIsEnable(String email);
}
