package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        List<Long> answersIdList = answerDtoList.stream()
                .map(AnswerDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<CommentDto>> commentsDtoByAnswersIds = commentAnswerDtoDao.getCommentsDtoByAnswersIds(answersIdList);

        answerDtoList.forEach(answerDto -> answerDto.setComments(commentsDtoByAnswersIds.get(answerDto.getId())));

        return answerDtoList;
    }
}
