package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;

import java.util.Optional;

public interface QuestionDtoService extends PageDtoService<QuestionDto> {

    Optional<QuestionDto> getQuestionDtoById(long id);
}
