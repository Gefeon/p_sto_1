package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;

import java.util.List;
import java.util.Map;

public interface CommentDtoDao {
    Map<Long, List<CommentDto>> getCommentsDtoByAnswersIds(List<Long> answerIds);

    List<CommentDto> getCommentDtoListByQuestionId(Long id);

    List<QuestionCommentDto> getQuestionCommentDtoById(Long id);
}
