package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.*;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDtoServiceImpl extends PageDtoServiceImpl<UserDto> implements UserDtoService {

    private final TagDtoDao tagDtoDao;
    private final UserDtoDao userDtoDao;

    public UserDtoServiceImpl(TagDtoDao tagDtoDao, UserDtoDao userDtoDao) {
        this.tagDtoDao = tagDtoDao;
        this.userDtoDao = userDtoDao;
    }

    @Override
    @Transactional
    public PageDto<UserDto> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {

        PageDto<UserDto> pageDto = super.getPage(currentPageNumber, itemsOnPage, map);
        List<UserDto> userDtos = pageDto.getItems();

        List<Long> userIds = userDtos.stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<TagDto>> tagsMap = tagDtoDao.getMapTagsByUserIds(userIds);
        for (UserDto userDto : userDtos) {
            userDto.setTags(tagsMap.get(userDto.getId()));
        }

        pageDto.setItems(userDtos);
        return pageDto;
    }

    @Override
    @Transactional
    public Optional<UserSupplierDto> getUserDtoById(Long id) {
        return userDtoDao.getUserDtoById(id);
    }
}
