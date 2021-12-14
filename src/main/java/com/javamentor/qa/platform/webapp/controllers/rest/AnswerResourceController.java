package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteAnswerService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.DOWN_VOTE;
import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.UP_VOTE;


@Api(tags = {SwaggerConfig.ANSWER_CONTROLLER})
@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
@RequiredArgsConstructor
public class AnswerResourceController {

    private final AnswerService answerService;
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
    //==================================================================================================================
    @Operation(summary = "Vote up for answer", responses = {
            @ApiResponse(responseCode = "200", description = "Vote up successful. Author's reputation increased"),
            @ApiResponse(responseCode = "400", description = "Cannot vote")})
    @PostMapping("{answerId}/upVote")
    public ResponseEntity<?> upVote(@PathVariable final Long questionId,
                                    @PathVariable final Long answerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long totalCount = voteAnswerService.vote(answerId, user, UP_VOTE);
        return Objects.isNull(totalCount) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(totalCount);
    }

    @Operation(summary = "Vote up for answer", responses = {
            @ApiResponse(responseCode = "200", description = "Vote down successful. Author's reputation decreased"),
            @ApiResponse(responseCode = "400", description = "Cannot vote")})
    @PostMapping("{answerId}/downVote")
    public ResponseEntity<?> downVote(@PathVariable final Long questionId,
                                      @PathVariable final Long answerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long totalCount = voteAnswerService.vote(answerId, user, UP_VOTE);
        return Objects.isNull(totalCount) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(totalCount);
    }
}

