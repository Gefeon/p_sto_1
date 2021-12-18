package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.*;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {

    private final QuestionDtoDao questionDao;
    private final TagDtoDao tagDtoDao;

    @Autowired
    public QuestionDtoServiceImpl(QuestionDtoDao questionDao, TagDtoDao tagDtoDao) {
        this.questionDao = questionDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public QuestionDto getQuestionDtoById(long id) {
        QuestionDto questionDto = null;
        try {
            questionDto = questionDao.getQuestionDtoById(id);
            questionDto.setListTagDto(tagDtoDao.getTagDtoList(id));
        } catch (NullPointerException e) {
            System.out.println("Вопроса по id: " + id + " не существует");
        }
        return questionDto;
    }
}
