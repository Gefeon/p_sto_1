package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserSupplierDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserDtoService extends PageDtoService<UserDto> {

    Optional<UserSupplierDto> getUserDtoById(Long id);
}
