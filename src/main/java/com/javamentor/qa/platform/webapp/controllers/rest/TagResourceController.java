package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = SwaggerConfig.RESOURCE_TAG_CONTROLLER)
@RestController
@RequestMapping("/api/user/tag")
public class TagResourceController {
    private final TagDtoService tagService;

    public TagResourceController(TagDtoService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(path = "/related")
    @Operation(summary = "Get related tags dto", responses = {
            @ApiResponse(description = "Get related tags success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RelatedTagsDto.class)))
    })
    public ResponseEntity<?> getRelatedTagsDto() {
        List<RelatedTagsDto> relatedTags = tagService.getRelatedTagsDto();
        return ResponseEntity.ok(relatedTags);
    }

    @Operation(summary = "Get ignored tags dto", responses = {
            @ApiResponse(description = "Get a list of ignored tags successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDto.class)))
    })
    @GetMapping("/ignored")
    public ResponseEntity<?> getIgnoredTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> ignoredTags = tagService.getIgnoredTagsByUserId(user.getId());
        return ResponseEntity.ok(ignoredTags);
    }

    @GetMapping("/tracked")
    @Operation(summary = "Get tracked tags dto", responses = {
            @ApiResponse(description = "Get a list of tracked tags successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDto.class)))
    })
    public ResponseEntity<?> getAllTrackedTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> trackedTags = tagService.getTrackedTagsByUserId(user.getId());
        return ResponseEntity.ok(trackedTags);
    }

    @Operation(summary = "Get 6 tags by it's first letters", responses = {
            @ApiResponse(description = "successfully get tags from DB", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Tag.class)))
    })
    @GetMapping("/letters")
    public ResponseEntity<?> getTagsByLetters(
            @ApiParam(value = "Letters for the next desired tag to attach to question")
            @RequestParam(required = false) String letters) {
        List<TagDto> tags = tagService.getTagsByLetters(letters);
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Get page of tags with pagination selected by name", responses = {
            @ApiResponse(description = " success", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class)))),
            @ApiResponse(description = "There is no curPage in the url or the parameters in the url are wrong", responseCode = "400")
    })
    @GetMapping("/name")
    public ResponseEntity<?> getPaginationTagsByName(@RequestParam int currPage, @RequestParam(required = false, defaultValue = "10") int items) {
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "TagPaginationByName");
        return ResponseEntity.ok(tagService.getPage(currPage, items, map));
    }
}
