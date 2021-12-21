package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        QuestionDto questionDto = questionDao.getQuestionDtoById(id);
        if (questionDto != null) {
            questionDto.setListTagDto(tagDtoDao.getTagDtoListByQuestionId(id));
        }
        return questionDto;
    }
}
