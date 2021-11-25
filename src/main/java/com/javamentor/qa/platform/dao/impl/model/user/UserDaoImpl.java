package com.javamentor.qa.platform.dao.impl.model.user;

import com.javamentor.qa.platform.dao.abstracts.model.user.UserDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {
}
