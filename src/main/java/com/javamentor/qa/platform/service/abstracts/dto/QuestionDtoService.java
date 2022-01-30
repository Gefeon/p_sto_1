package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface QuestionDtoService extends PageDtoService<QuestionDto> {

    Optional<QuestionDto> getQuestionDtoById(long id);

    List<QuestionCommentDto> getQuestionCommentDtoById(Long id);
}
