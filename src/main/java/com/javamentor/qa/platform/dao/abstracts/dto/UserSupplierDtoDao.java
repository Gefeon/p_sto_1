package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserSupplierDto;

import java.util.Optional;

public interface UserSupplierDtoDao {
    Optional<UserSupplierDto> getUserDtoById(Long id);
}
