package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.IgnoredTagDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.RelatedTagsDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TrackedTagDtoService;
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

    private final TrackedTagDtoService trackedTagService;
    private final IgnoredTagDtoService ignoredTagService;
    private final RelatedTagsDtoService relatedTagsDtoService;

    public TagResourceController(TrackedTagDtoService trackedTagService, IgnoredTagDtoService ignoredTagService, RelatedTagsDtoService relatedTagsDtoService) {
        this.trackedTagService = trackedTagService;
        this.ignoredTagService = ignoredTagService;
        this.relatedTagsDtoService = relatedTagsDtoService;
    }

    @GetMapping(path = "/related")
    @Operation(summary = "Get related tags dto", responses = {
            @ApiResponse(description = "Get related tags success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RelatedTagsDto.class)))
    })
    public ResponseEntity<?> getRelatedTagsDto() {
        List<RelatedTagsDto> relatedTags = relatedTagsDtoService.getRelatedTagsDto();
        return ResponseEntity.ok(relatedTags);
    }

    @Operation(summary = "Get ignored tags from authenticated user", responses = {
            @ApiResponse(description = "Get ignored tags success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Tag.class)))
    })
    @GetMapping("/ignored")
    public ResponseEntity<?> getAllIgnoredTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> ignoredTags = ignoredTagService.getTagsByUserId(user.getId());
        return ResponseEntity.ok(ignoredTags);
    }

    @GetMapping("/tracked")
    @Operation(summary = "Get tracked tags from authenticated user", responses = {
            @ApiResponse(description = "Get tracked tags success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Tag.class)))
    })
    public ResponseEntity<?> getAllTrackedTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> trackedTags = trackedTagService.getTagsByUserId(user.getId());
        return ResponseEntity.ok(trackedTags);
    }
}
