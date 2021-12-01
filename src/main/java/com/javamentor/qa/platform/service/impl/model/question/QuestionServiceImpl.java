package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.QuestionDao;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private final TagService tagService;
    private final UserService userService;

    public QuestionServiceImpl(QuestionDao questionDao, TagService tagService, UserService userService) {
        super(questionDao);
        this.tagService = tagService;
        this.userService = userService;
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
