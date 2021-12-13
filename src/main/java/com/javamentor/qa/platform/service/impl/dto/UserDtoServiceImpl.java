package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class UserDtoServiceImpl implements UserDtoService{

    private final UserDtoDao userDtoDao;
    private final PageDtoService<UserDto> pageDtoService;

    @Autowired
    public UserDtoServiceImpl(UserDtoDao userDtoDao, PageDtoService<UserDto> pageDtoService) {
        this.userDtoDao = userDtoDao;
        this.pageDtoService = pageDtoService;
    }

    @Override
    @Transactional
    public Optional<UserDto> getUserDtoById(Long id) {
        return userDtoDao.getUserDtoById(id);
    }

    @Override
    public PageDto<UserDto> getPage(int currPage, int items, Map<Object, Object> map) {
        return pageDtoService.getPage(currPage, items, map);
    }
}
