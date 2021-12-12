package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionGetDto;

public interface QuestionGetDtoDao {

    QuestionGetDto getQuestionDtoById(long id);
}
