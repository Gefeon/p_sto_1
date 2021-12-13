package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;

import java.util.Map;
import java.util.Optional;

public interface UserDtoService {
    Optional<UserDto> getUserDtoById(Long id);

    PageDto<UserDto> getPage(int currPage, int items, Map<Object, Object> map);
}
