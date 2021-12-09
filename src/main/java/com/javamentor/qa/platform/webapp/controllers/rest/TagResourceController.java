package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.IgnoredTagDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.RelatedTagsDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = SwaggerConfig.RESOURCE_TAG_CONTROLLER)
@RestController
@RequestMapping("/api/user/tag")
public class TagResourceController {

    private final IgnoredTagDtoService ignoredTagService;
    private final RelatedTagsDtoService relatedTagsDtoService;
    private final TagDtoService tagService;

    public TagResourceController(IgnoredTagDtoService ignoredTagService, RelatedTagsDtoService relatedTagsDtoService, TagDtoService tagService) {
        this.ignoredTagService = ignoredTagService;
        this.relatedTagsDtoService = relatedTagsDtoService;
        this.tagService = tagService;
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
                    content = @Content(schema = @Schema(implementation = IgnoredTag.class)))
    })
    @GetMapping("/ignored")
    public ResponseEntity<?> getAllIgnoredTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> ignoredTags = ignoredTagService.getTagsByUserId(user.getId());
        return ResponseEntity.ok(ignoredTags);
    }

    @Operation(summary = "Get 6 tags by it's first letters", responses = {
            @ApiResponse(description = "successfully get tags from DB", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Tag.class)))
    })
    @PostMapping("/letters")
    public ResponseEntity<?> getTagsByFirstLetters(@RequestBody Map<String, String> json) {
        List<TagDto> tags = tagService.getTagsByFirstLetters(json.get("letters"));
        return ResponseEntity.ok(tags);
    }
}
