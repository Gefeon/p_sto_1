package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

import java.util.List;
import java.util.Map;

public interface CommentQuestionDtoDao {

    Map<Long, List<CommentDto>> getMapCommentsByQuestionIds(List<Long> questionIds);

    List<CommentDto> getCommentDtoListByQuestionId(Long id);
}
