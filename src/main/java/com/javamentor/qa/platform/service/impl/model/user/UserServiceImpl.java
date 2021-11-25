package com.javamentor.qa.platform.service.impl.model.user;

import com.javamentor.qa.platform.dao.abstracts.model.user.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ReadWriteServiceImpl<User, Long> implements UserService {

    public UserServiceImpl(UserDao userDao) {
        super(userDao);
    }
}
