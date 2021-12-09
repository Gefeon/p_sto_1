package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.AnswerService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.UP_VOTE;


@Api(tags = {SwaggerConfig.ANSWER_CONTROLLER})
@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
public class AnswerResourceController {

    private final AnswerService answerService;

    public AnswerResourceController(AnswerService answerService) {
        this.answerService = answerService;
    }

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

    @PostMapping("{id}/upVote")
    public ResponseEntity<?> upVote(@PathVariable("questionId") Long questionId,
                                    @PathVariable("id") Long answerId) {
        System.out.println("**UpVote** questionId = " + questionId + "; answerId = " + answerId);
//        User user = userService.findByEmail(auth.getName()).orElseThrow(NoSuchElementException::new);
//        Answer answer = answerService.getById(answerId).get();
//
//        if (answerService.canUserVote(answer.getId(), user.getId(), UP_VOTE)) {
//            Long count = answerService.voteOnAnswer(answer, user, UP_VOTE);
//            System.out.println("Count: " + count);
//            return ResponseEntity.ok("count here: " + count);
//        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("{id}/downVote")
    public ResponseEntity<?> downVote(@PathVariable("questionId") Long questionId, @PathVariable("id") Long answerId) {
        System.out.println("**DownVote** questionId = " + questionId + "; answerId = " + answerId);
        return ResponseEntity.ok("**DownVote** questionId = " + questionId + "; answerId = " + answerId);
    }
}

