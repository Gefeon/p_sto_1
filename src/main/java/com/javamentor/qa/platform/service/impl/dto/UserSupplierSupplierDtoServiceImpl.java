package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserSupplierDtoDao;
import com.javamentor.qa.platform.models.dto.UserSupplierDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserSupplierDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserSupplierSupplierDtoServiceImpl extends PageDtoServiceImpl<UserSupplierDto> implements UserSupplierDtoService {

    private final UserSupplierDtoDao userSupplierDtoDao;

    @Autowired
    public UserSupplierSupplierDtoServiceImpl(UserSupplierDtoDao userSupplierDtoDao) {
        this.userSupplierDtoDao = userSupplierDtoDao;
    }

    @Override
    @Transactional
    public Optional<UserSupplierDto> getUserDtoById(Long id) {
        return userSupplierDtoDao.getUserDtoById(id);
    }
}
