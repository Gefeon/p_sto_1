package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.*;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteQuestionService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import com.javamentor.qa.platform.webapp.converters.mapper.QuestionMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(tags = {SwaggerConfig.QUESTION_CONTROLLER})
@RestController
@RequestMapping("/api/user/question")
@Validated
public class QuestionResourceController {

    private final QuestionMapper questionMapper;

    private final QuestionService questionService;

    private final QuestionDtoService questionDtoService;

    private final VoteQuestionService voteQuestionService;

    public QuestionResourceController(QuestionMapper questionMapper,
                                      QuestionService questionService,
                                      QuestionDtoService questionDtoService,
                                      VoteQuestionService voteQuestionService) {
        this.questionMapper = questionMapper;
        this.questionService = questionService;
        this.questionDtoService = questionDtoService;
        this.voteQuestionService = voteQuestionService;
    }

    @Operation(summary = "add new question", responses = {
            @ApiResponse(description = "Question was added to DB", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(description = "Received tags, description or title were empty or null", responseCode = "400")
    })
    @PostMapping()
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionDtoById(@PathVariable("id") Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<QuestionDto> questionDto = questionDtoService.getQuestionDtoByIdAndUserAuthId(id, user.getId());
        return questionDto.isEmpty()
                ? new ResponseEntity<>("Missing question or invalid id", HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

    @Operation(summary = "Up Vote on this Question", responses = {
            @ApiResponse(description = "Vote and get sum of votes on this question", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(description = "Vote for this question already exists", responseCode = "400", content = @Content)
    })
    @PostMapping("/{questionId}/upVote")
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
    @PostMapping("/{questionId}/downVote")
    public ResponseEntity<?> downVote(@PathVariable("questionId") Long questionId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (voteQuestionService.checkIfVoteQuestionDoesNotExist(questionId, user.getId())) {
            return ResponseEntity.ok(voteQuestionService.voteAndGetCountVoteQuestionFotThisQuestion(
                    questionId, VoteType.DOWN_VOTE, user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vote for this question already exists");
    }

    @Operation(summary = "counts all questions", responses = {
            @ApiResponse(description = "Question was counted", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        Long count = questionService.countQuestions();
        return ResponseEntity.ok(count);
    }

    @GetMapping(path = "tag/{id}")
    @Operation(summary = "Get page of questions with pagination selected by tag id", responses = {
            @ApiResponse(description = " success", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionDto.class)))),
            @ApiResponse(description = "there isn`t curPage parameter in url or parameters in url are not positives numbers", responseCode = "400")
    })
    public ResponseEntity<?> getPageDtoOfQuestionDtoByTagId(
            @Parameter(description = "Tag id", required = true)
            @PathVariable Long id,
            @ApiParam(value = "positive number representing number of current page", required = true)
            @RequestParam @Positive(message = "current page must be positive number") int currPage,
            @ApiParam(value = "positive number representing number of items to show on page")
            @RequestParam(required = false, defaultValue = "10") @Positive(message = "items must be positive number") int items) {
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "QuestionDtoPaginationByTag");
        map.put("tagId", id);
        map.put("userAuth", userAuth);
        PageDto<QuestionViewDto> page = questionDtoService.getPage(currPage, items, map);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Get list of questions by date", responses = {
            @ApiResponse(description = "Got list of questions", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionDto.class)))),
            @ApiResponse(description = "Too much value of parameters currPage or items - return empty List<QuestionDto>",
                    responseCode = "200"),
            @ApiResponse(description = "Wrong parameters or absent current page", responseCode = "400", content = @Content)
    })
    @GetMapping("new")
    public ResponseEntity<?> getQuestionByDate(@RequestParam int currPage,
                                 @RequestParam(required = false, defaultValue = "10") int items,
                                 @RequestParam(required = false, defaultValue = "0") List<Long> ignoredTags,
                                 @RequestParam(required = false) List<Long> trackedTags) {
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "QuestionByDate");
        map.put("ignoredTags", ignoredTags);
        map.put("trackedTags", trackedTags);
        map.put("userAuth", userAuth);
        return ResponseEntity.ok(questionDtoService.getPage(currPage, items, map));
    }


    @Operation(summary = "Get page with pagination by users' persist datetime", responses = {
            @ApiResponse(description = " success", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(description = "there isn`t curPage parameter in url or parameters in url are not positives numbers", responseCode = "400")
    })
    @GetMapping()
    public ResponseEntity<?> getAllQuestionDtos(
            @ApiParam(value = "positive number representing number of current page", required = true)
            @RequestParam @Positive(message = "current page must be positive number") int currPage,
            @ApiParam(value = "positive number representing number of items to show on page")
            @RequestParam(required = false, defaultValue = "10") @Positive(message = "items must be positive number") int items,
            @ApiParam(value = "list of tracked tags attached to question")
            @RequestParam(required = false)  List<@Positive(message = "ids of tracked tags must be positive numbers") Long> trackedId,
            @ApiParam(value = "list of ignored tags attached to question")
            @RequestParam(required = false) List<@Positive(message = "ids of ignored tags must be positive numbers") Long> ignoredId) {
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "AllQuestions");
        map.put("trackedIds", trackedId);
        map.put("ignoredIds", ignoredId);
        map.put("userAuth", userAuth);
        PageDto<QuestionViewDto> page = questionDtoService.getPage(currPage, items, map);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Getting all comments on the ID of the question", responses = {
            @ApiResponse(description = "Successfully retrieving a list of comments by question ID", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = QuestionCommentDto.class))),
            @ApiResponse(description = "There is no comment on this ID question in the database", responseCode = "400")
    })
    @GetMapping("/{id}/comment")
    public ResponseEntity<?> getQuestionCommentDtoById(@PathVariable("id") Long id) {
        List<QuestionCommentDto> questionCommentDtoList = questionDtoService.getQuestionCommentDtoById(id);
        return new ResponseEntity<>(questionCommentDtoList, HttpStatus.OK);
    }


    @GetMapping("/noAnswer")
    @Operation(summary = "Get page pagination questions with no answer", responses = {
            @ApiResponse(description = "Get page dto of question dto success", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(description = "Wrong parameters current page or items", responseCode = "400", content = @Content)
    })
    public ResponseEntity<?>noAnswerQuestion (@RequestParam int currPage,
                                              @RequestParam(required = false, defaultValue = "10") int items,
                                              @RequestParam(required = false, defaultValue = "0") List<Long> ignoredTags,
                                              @RequestParam(required = false) List<Long> trackedTags) {
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "QuestionNoAnswer");
        map.put("ignoredTags", ignoredTags);
        map.put("trackedTags", trackedTags);
        map.put("userAuth", userAuth);
        PageDto<QuestionViewDto> page = questionDtoService.getPage(currPage, items, map);
        return ResponseEntity.ok(page);
    }


}
