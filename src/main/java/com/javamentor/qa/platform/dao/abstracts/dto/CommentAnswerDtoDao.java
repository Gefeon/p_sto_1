package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

import java.util.List;
import java.util.Map;

public interface CommentAnswerDtoDao {
    Map<Long, List<CommentDto>> getCommentsDtoByAnswersIds(List<Long> answerIds);
}
