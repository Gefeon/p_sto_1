package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.question.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteAnswerService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.DOWN_VOTE;
import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.UP_VOTE;



@Api(tags = {SwaggerConfig.ANSWER_CONTROLLER})
@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
public class AnswerResourceController {

    private final AnswerService answerService;
    private final AnswerDtoService answerDtoService;

    public AnswerResourceController(AnswerService answerService, AnswerDtoService answerDtoService, VoteAnswerService voteAnswerService) {
        this.answerService = answerService;
        this.answerDtoService = answerDtoService;
        this.voteAnswerService = voteAnswerService;
    }
    private final VoteAnswerService voteAnswerService;

    @Operation(summary = "Delete an answer by id", responses = {
            @ApiResponse(description = "Answer was deleted from DB", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Answer.class))),
            @ApiResponse(description = "Received if no answer with such id exists in DB", responseCode = "400")
    })
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long questionId, @PathVariable Long answerId) {
        if (answerService.existsById(answerId)) {
            answerService.deleteById(answerId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("No answer with such id exists in DB", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Get list of answers by question id", responses = {
            @ApiResponse(description = "Got list of answers", responseCode = "200",
                        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AnswerDto.class)))),
            @ApiResponse(description = "No answers with such question id - return empty list", responseCode = "200"),
            @ApiResponse(description = "Wrong type of question id", responseCode = "400")
    })
    @GetMapping
    public ResponseEntity<?> getAnswerByQuestionId(@PathVariable Long questionId) {
        return  ResponseEntity.ok(answerDtoService.getAnswerById(questionId));
    }

    @Operation(summary = "Vote up for answer", responses = {
            @ApiResponse(responseCode = "200", description = "Vote up successful. Author's reputation increased"),
            @ApiResponse(responseCode = "400", description = "Cannot vote")})
    @PostMapping("{answerId}/upVote")
    public ResponseEntity<?> upVote(@PathVariable final Long questionId,
                                    @PathVariable final Long answerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (voteAnswerService.isUserNonVoted(answerId, user.getId())) {
            Long totalCount = voteAnswerService.vote(answerId, user, UP_VOTE);
            return ResponseEntity.ok(totalCount) ;
        }
        return ResponseEntity.badRequest().body("User is already voted");
    }

    @Operation(summary = "Vote down for answer", responses = {
            @ApiResponse(responseCode = "200", description = "Vote down successful. Author's reputation decreased"),
            @ApiResponse(responseCode = "400", description = "Cannot vote")})
    @PostMapping("{answerId}/downVote")
    public ResponseEntity<?> downVote(@PathVariable final Long questionId,
                                      @PathVariable final Long answerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (voteAnswerService.isUserNonVoted(answerId, user.getId())) {
            Long totalCount = voteAnswerService.vote(answerId, user, DOWN_VOTE);
            return ResponseEntity.ok(totalCount);
        }
        return ResponseEntity.badRequest().body("User is already voted");
    }
}

