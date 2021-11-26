package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.question.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Api(tags = {SwaggerConfig.ANSWER_CONTROLLER})
@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    public ResourceAnswerController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @Operation(summary = "Delete an answer by id", responses = {
            @ApiResponse(description = "Answer was deleted from DB", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Answer.class))),
            @ApiResponse(description = "Received if no answer with such id exists in DB", responseCode = "400")
    })
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long questionId, @PathVariable Long answerId) {
        try {
            Answer answer = answerService.getById(answerId).orElseThrow();
            answerService.delete(answer);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
