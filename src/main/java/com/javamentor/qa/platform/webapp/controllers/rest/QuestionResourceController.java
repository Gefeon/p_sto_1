package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.mapper.QuestionMapper;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class QuestionResourceController {

    private final QuestionMapper questionMapper;
    private final TagService tagService;
    private final UserService userService;
    private final QuestionService questionService;

    public QuestionResourceController(QuestionMapper questionMapper, TagService tagService,
                                      UserService userService, QuestionService questionService) {
        this.questionMapper = questionMapper;
        this.tagService = tagService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @PostMapping("/question")
    public QuestionDto getUserDto(@Valid @RequestBody QuestionCreateDto questionCreateDto) {
        Question question = questionMapper.toModel(questionCreateDto);
        List<Tag> tags = new ArrayList<>();
        int counter = 0;
        for (Tag tag : question.getTags()) {
            Tag newTag;
            if (tagService.existsByName(questionCreateDto.getTags().get(counter++).getName())) {
                newTag = tagService.getByName(questionCreateDto.getTags().get(counter++).getName()).orElse(null);
            } else {
                newTag = new Tag();
                newTag.setName(tag.getName());
                tagService.persist(newTag);
            }
            tags.add(newTag);
        }
        question.setTags(tags);

        //TODO: instead of using userFrom DB we need attach
        // to Question @AuthenticationPrincipal
        User user = userService.getById(1L).orElseThrow();
        question.setUser(user);

        questionService.persist(question);
        QuestionDto dto = questionMapper.toDto(question);
        System.out.println("Вот");
        return dto;
    }
}
