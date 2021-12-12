package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionGetDto;

public interface QuestionGetDtoService {

    QuestionGetDto getQuestionDtoById(long id);
}