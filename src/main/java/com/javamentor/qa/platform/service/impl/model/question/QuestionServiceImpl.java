package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.QuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    @PersistenceContext
    EntityManager entityManager;

    public QuestionServiceImpl(QuestionDao questionDao) {
        super(questionDao);
    }

    @Override
    @SuppressWarnings("unchecked") //because row use of Query is bad practice
    public Optional<Question> getWithTagsById(Long id) {
        String hql = "FROM Question q LEFT JOIN FETCH q.tags WHERE q.id = :id ";
        TypedQuery<Question> query = (TypedQuery<Question>) entityManager.createQuery(hql).setParameter("id", id);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
