package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionGetDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionGetDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionGetDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestionGetDtoServiceImpl implements QuestionGetDtoService {

    private final QuestionGetDtoDao questionDao;
    private final TagDtoDao tagDtoDao;

    @Autowired
    public QuestionGetDtoServiceImpl(QuestionGetDtoDao questionDao, TagDtoDao tagDtoDao) {
        this.questionDao = questionDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public QuestionGetDto getQuestionDtoById(long id) {
        QuestionGetDto questionGetDto = questionDao.getQuestionDtoById(id);
        questionGetDto.setListTagDto(tagDtoDao.getTagDtoList());
        return questionGetDto;
    }
}
