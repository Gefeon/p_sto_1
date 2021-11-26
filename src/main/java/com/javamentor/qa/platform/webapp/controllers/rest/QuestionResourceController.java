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
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {SwaggerConfig.QUESTION_CONTROLLER})
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

    @Operation(summary = "add new question", responses = {
            @ApiResponse(description = "Question was added to DB", responseCode = "201",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(description = "Received tags, description or title were empty or null", responseCode = "400")
    })
    @PostMapping("/question")
    public ResponseEntity<QuestionDto> addQuestion(@Valid @RequestBody QuestionCreateDto questionCreateDto) {
        Question question = questionMapper.toModel(questionCreateDto);
        List<Tag> tags = new ArrayList<>();
        int counter = 0;
        for (Tag tag : question.getTags()) {
            Tag newTag;
            if (tagService.existsByName(questionCreateDto.getTags().get(counter).getName())) {
                newTag = tagService.getByName(questionCreateDto.getTags().get(counter).getName()).orElse(null);
            } else {
                newTag = new Tag();
                newTag.setName(tag.getName());
                tagService.persist(newTag);
            }
            tags.add(newTag);
            counter++;
        }
        question.setTags(tags);

        //TODO: instead of using userFromDB we need attach
        // to Question @AuthenticationPrincipal
        User user = userService.getAll().get(0);
        question.setUser(user);

        questionService.persist(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionMapper.toDto(question));
    }
}
