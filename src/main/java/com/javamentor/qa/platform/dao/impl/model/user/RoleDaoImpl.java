package com.javamentor.qa.platform.dao.impl.model.user;

import com.javamentor.qa.platform.dao.abstracts.model.user.RoleDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.user.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends ReadWriteDaoImpl<Role, Long> implements RoleDao {
}
