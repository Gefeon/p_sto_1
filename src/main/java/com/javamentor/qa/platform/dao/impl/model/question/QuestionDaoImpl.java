package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.QuestionDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements QuestionDao {
}
