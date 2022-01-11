package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserSupplierDto;

import java.util.Optional;

public interface UserSupplierDtoService extends PageDtoService<UserSupplierDto> {
    Optional<UserSupplierDto> getUserDtoById(Long id);
}
