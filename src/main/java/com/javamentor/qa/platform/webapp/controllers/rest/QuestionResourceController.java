package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.mapper.QuestionMapper;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteQuestionService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.Optional;

@Api(tags = {SwaggerConfig.QUESTION_CONTROLLER})
@RestController
@RequestMapping("/api/user")
public class QuestionResourceController {

    private final QuestionMapper questionMapper;

    private final QuestionDtoService questionGetDtoService;

    private final QuestionService questionService;

    public QuestionResourceController(QuestionMapper questionMapper, QuestionDtoService questionGetDtoService, QuestionService questionService) {
    private final VoteQuestionService voteQuestionService;

    public QuestionResourceController(QuestionMapper questionMapper,
                                      QuestionService questionService,
                                      VoteQuestionService voteQuestionService) {
        this.questionMapper = questionMapper;
        this.questionGetDtoService = questionGetDtoService;
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
    }

    @Operation(summary = "add new question", responses = {
            @ApiResponse(description = "Question was added to DB", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(description = "Received tags, description or title were empty or null", responseCode = "400")
    })
    @PostMapping("/question")
    public ResponseEntity<QuestionDto> addQuestion(
            @ApiParam(value = "A JSON object containing question title, description and tags", required = true)
            @Valid @RequestBody final QuestionCreateDto questionCreateDto) {
        Question question = questionMapper.toModel(questionCreateDto);
        questionService.persist(question);
        return ResponseEntity.status(HttpStatus.OK).body(questionMapper.persistConvertToDto(question));
    }

    @Operation(summary = "Getting dto by id", responses = {
            @ApiResponse(description = "Successful receipt of dto questions on ID", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(description = "Received if no question with such id exists in DB", responseCode = "400")
    })

    @GetMapping("/question/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        Optional<QuestionDto> questionDto = questionGetDtoService.getQuestionDtoById(id);
        return new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

    @Operation(summary = "Up Vote on this Question", responses = {
            @ApiResponse(description = "Vote and get sum of votes on this question", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(description = "Vote for this question already exists", responseCode = "400", content = @Content)
    })
    @PostMapping("/question/{questionId}/upVote")
    public ResponseEntity<?> upVote(@PathVariable("questionId") Long questionId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (voteQuestionService.checkIfVoteQuestionDoesNotExist(questionId, user.getId())) {
            return ResponseEntity.ok(voteQuestionService.voteAndGetCountVoteQuestionFotThisQuestion(
                    questionId, VoteType.UP_VOTE, user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vote for this question already exists");
    }

    @Operation(summary = "Down Vote on this Question", responses = {
            @ApiResponse(description = "Vote and get sum of votes on this question", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(description = "Vote for this question already exists", responseCode = "400", content = @Content)
    })
    @PostMapping("/question/{questionId}/downVote")
    public ResponseEntity<?> downVote(@PathVariable("questionId") Long questionId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (voteQuestionService.checkIfVoteQuestionDoesNotExist(questionId, user.getId())) {
            return ResponseEntity.ok(voteQuestionService.voteAndGetCountVoteQuestionFotThisQuestion(
                    questionId, VoteType.DOWN_VOTE, user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vote for this question already exists");
    }
}
