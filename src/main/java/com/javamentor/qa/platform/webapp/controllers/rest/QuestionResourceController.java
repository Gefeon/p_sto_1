package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.mapper.QuestionMapper;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Api(tags = {SwaggerConfig.QUESTION_CONTROLLER})
@RestController
@RequestMapping("/api/user")
public class QuestionResourceController {

    private final QuestionMapper questionMapper;

    private final QuestionService questionService;

    private final VoteQuestionService voteQuestionService;

    public QuestionResourceController(QuestionMapper questionMapper,
                                      QuestionService questionService,
                                      VoteQuestionService voteQuestionService) {
        this.questionMapper = questionMapper;
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

    @Operation(summary = "Up Vote on this Question", responses = {
            @ApiResponse(description = "Get sum of votes on this question", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(description = "No votes found", responseCode = "404", content = @Content)
    })
    @PostMapping("/question/{questionId}/upVote")
    public ResponseEntity<?> upVote(@PathVariable("questionId") Long id){
       Optional<Long> sum = voteQuestionService.voteAndGetSumOfVotes(id, VoteType.UP_VOTE);
       return sum.isEmpty()
               ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No votes found")
               : ResponseEntity.ok(sum.get());
    }

    @Operation(summary = "Down Vote on this Question", responses = {
            @ApiResponse(description = "Get sum of votes on this question", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(description = "No votes found", responseCode = "404", content = @Content)
    })
    @PostMapping("/question/{questionId}/downVote")
    public ResponseEntity<?> downVote(@PathVariable("questionId") Long id){
        Optional<Long> sum = voteQuestionService.voteAndGetSumOfVotes(id, VoteType.DOWN_VOTE);
        return sum.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No votes found")
                : ResponseEntity.ok(sum.get());
    }
}
