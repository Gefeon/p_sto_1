package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionCommentDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionCommentDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionCommentDtoServiceImpl implements QuestionCommentDtoService {

    private  final QuestionCommentDtoDao commentDtoDao;

    public QuestionCommentDtoServiceImpl(QuestionCommentDtoDao commentDtoDao) {
        this.commentDtoDao = commentDtoDao;
    }

    @Override
    @Transactional
    public List<QuestionCommentDto> getQuestionCommentDtoById(Long id) {
        return commentDtoDao.getQuestionCommentDtoById(id);
    }
}
