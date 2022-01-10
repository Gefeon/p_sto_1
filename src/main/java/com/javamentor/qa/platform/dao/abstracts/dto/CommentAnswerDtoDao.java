package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

import java.util.List;

public interface CommentAnswerDtoDao {

    List<CommentDto> getAnswerCommentDtoById(Long id);
}
