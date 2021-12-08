package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    //private final QuestionDao questionDao;

   // @Autowired
   // public QuestionServiceImpl (QuestionDao questionDao) {
       // this.questionDao = questionDao;
   // }
   // @Autowired
    public QuestionServiceImpl(QuestionDao questionDao) {
        super(questionDao);
    }

    @Override
    public Long countQuestions() { return questionDao.countQuestions();}
}
