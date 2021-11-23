package com.javamentor.qa.platform.service.impl.model.user;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.service.abstracts.model.user.RoleService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ReadWriteServiceImpl<Role, Long> implements RoleService {
    
    public RoleServiceImpl(ReadWriteDao<Role, Long> readWriteDao) {
        super(readWriteDao);
    }
}
