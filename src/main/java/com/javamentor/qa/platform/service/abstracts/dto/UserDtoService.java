package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;

import java.util.Optional;

public interface UserDtoService extends PageDtoService<UserDto> {

    Optional<UserDto> getUserDtoById(Long id);
}
