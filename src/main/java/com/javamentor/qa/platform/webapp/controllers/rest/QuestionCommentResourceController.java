package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionCommentDtoService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {SwaggerConfig.QUESTION_COMMENT_CONTROLLER})
@RestController
@RequestMapping("/api/user/question")
@Validated
public class QuestionCommentResourceController {

    private final QuestionCommentDtoService questionCommentDtoService;

    public QuestionCommentResourceController(QuestionCommentDtoService questionCommentDtoService) {
        this.questionCommentDtoService = questionCommentDtoService;
    }

    @Operation(summary = "Getting all comments on the ID of the question", responses = {
            @ApiResponse(description = "Successfully retrieving a list of comments by question ID", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = QuestionCommentDto.class))),
            @ApiResponse(description = "There is no comment on this ID question in the database", responseCode = "400")
    })
    @GetMapping("/{id}/comment")
    public ResponseEntity<?> getQuestionCommentDtoById(@PathVariable("id") Long id) {
        List<QuestionCommentDto> questionCommentDtoList = questionCommentDtoService.getQuestionCommentDtoById(id);
        if (questionCommentDtoList.isEmpty()) {
            return new ResponseEntity<>("Missing comment or invalid ID", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(questionCommentDtoList, HttpStatus.OK);
    }
}
