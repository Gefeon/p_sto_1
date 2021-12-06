package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.QuestionDao;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {


    private User user;
    private final TagService tagService;
    private final UserDetailsService userDetailsService;

    public QuestionServiceImpl(QuestionDao questionDao, TagService tagService, UserDetailsService userDetailsService) {
        super(questionDao);
        this.tagService = tagService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void persist(Question question){

        Set<String> tagNames = question.getTags().stream().map(Tag::getName).collect(Collectors.toSet());

        List<Tag> existedTags = tagService.getByAllNames(tagNames);
        Set<String> existedTagNames = existedTags.stream().map(Tag::getName).collect(Collectors.toSet());

        List<Tag> tagsToPersist = question.getTags();
        tagsToPersist.removeIf(tag -> existedTagNames.contains(tag.getName()));

        if(!tagsToPersist.isEmpty()) {
            tagService.persistAll(tagsToPersist);
        }
        List<Tag> managedTags = new ArrayList<>(tagsToPersist);
        managedTags.addAll(existedTags);
        question.setTags(managedTags);

        User user = (User) userDetailsService.loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        question.setUser(user);

        super.persist(question);
    }

}
