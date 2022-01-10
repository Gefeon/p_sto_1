package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerDtoServiceImpl implements AnswerDtoService {

    private final AnswerDtoDao answerDtoDao;
    private final CommentAnswerDtoDao commentAnswerDtoDao;

    @Autowired
    public AnswerDtoServiceImpl(AnswerDtoDao answerDtoDao, CommentAnswerDtoDao commentAnswerDtoDao) {
        this.answerDtoDao = answerDtoDao;
        this.commentAnswerDtoDao = commentAnswerDtoDao;
    }

    @Override
    public List<AnswerDto> getAnswerById(Long id) {
        List<AnswerDto> answerDtoList = answerDtoDao.getAnswerById(id);
        answerDtoList.forEach(answerDto -> answerDto.setComments(commentAnswerDtoDao.getAnswerCommentDtoById(answerDto.getId())));
        return answerDtoList;
    }
}
