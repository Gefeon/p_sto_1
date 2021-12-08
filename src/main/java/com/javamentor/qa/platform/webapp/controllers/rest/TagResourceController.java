package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.IgnoredTagDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.RelatedTagsDtoService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = SwaggerConfig.RESOURCE_TAG_CONTROLLER)
@RestController
@RequestMapping("/api/user/tag")
public class TagResourceController {

    private final IgnoredTagDtoService ignoredTagService;
    private final RelatedTagsDtoService relatedTagsDtoService;

    public TagResourceController(IgnoredTagDtoService ignoredTagService, RelatedTagsDtoService relatedTagsDtoService) {
        this.ignoredTagService = ignoredTagService;
        this.relatedTagsDtoService = relatedTagsDtoService;
    }

    @GetMapping(path = "/related")
    @Operation(summary = "Get related tags dto", responses = {
            @ApiResponse(description = "Get related tags success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RelatedTagsDto.class))),
            @ApiResponse(description = "No related tags found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<?> getRelatedTagsDto() {
        List<RelatedTagsDto> relatedTags = relatedTagsDtoService.getRelatedTagsDto();
        return relatedTags.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No related tags found")
                : ResponseEntity.ok(relatedTags);
    }

    @Operation(summary = "Get ignored tags from authenticated user", responses = {
            @ApiResponse(description = "tags were retrieved from DB", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Tag.class))),
            @ApiResponse(description = "No ignored tags found", responseCode = "404", content = @Content)
    })
    @GetMapping("/ignored")
    public ResponseEntity<?> getAllIgnoredTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> ignoredTags = ignoredTagService.getTagsByUserId(user.getId());
        return ignoredTags.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ignored tags found")
                : ResponseEntity.ok(ignoredTags);
    }
}
