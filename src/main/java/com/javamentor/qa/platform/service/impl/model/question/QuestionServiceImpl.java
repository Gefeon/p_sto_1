package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    public QuestionServiceImpl(ReadWriteDao<Question, Long> readWriteDao) {
        super(readWriteDao);
    }
}
