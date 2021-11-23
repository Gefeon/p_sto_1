package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.QuestionDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements QuestionDao {
}
