package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.QuestionDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements QuestionDao {

    private final TagService tagService;
    private final UserService userService;

    @PersistenceContext
    EntityManager entityManager;

    public QuestionDaoImpl(TagService tagService, UserService userService) {
        this.tagService = tagService;
        this.userService = userService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Question> getWithTagsById(Long id) {
        String hql = "FROM Question q LEFT JOIN FETCH q.tags WHERE q.id = :id ";
        TypedQuery<Question> query = (TypedQuery<Question>) entityManager
                .createQuery(hql)
                .setParameter("id", id);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public void persist(Question question){
        List<String> namesToFetch = new ArrayList<>();
        List<Tag> tagsToPersist = new ArrayList<>();


        for (Tag tag : question.getTags()) {
            if (tagService.getAll().stream().anyMatch(tag1 -> tag1.getName().equals(tag.getName()))) {
                namesToFetch.add(tag.getName());
            } else {
                tagsToPersist.add(tag);
            }
        }
        tagService.persistAll(tagsToPersist);
        List<Tag> managedTags = new ArrayList<>(tagsToPersist);
        managedTags.addAll(tagService.getAllByNames(namesToFetch));
        question.setTags(managedTags);

        //TODO: instead of using userFromDB we need attach
        // to Question @AuthenticationPrincipal
        User user = userService.getAll().stream().findFirst().orElseThrow();
        question.setUser(user);

        super.persist(question);
    }
}
