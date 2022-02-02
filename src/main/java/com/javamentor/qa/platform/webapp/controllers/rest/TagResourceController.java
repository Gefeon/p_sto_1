package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.IgnoredTagDto;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TrackedTagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.question.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.abstracts.model.question.TrackedTagService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import com.javamentor.qa.platform.webapp.converters.mapper.IgnoredTagMapper;
import com.javamentor.qa.platform.webapp.converters.mapper.TrackedTagMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(tags = SwaggerConfig.RESOURCE_TAG_CONTROLLER)
@RestController
@RequestMapping("/api/user/tag")
public class TagResourceController {
    private final TagDtoService tagDtoService;
    private final TagService tagService;
    private final TrackedTagService trackedTagService;
    private final IgnoredTagService ignoredTagService;

    public TagResourceController(TagDtoService tagDtoService, TagService tagService, TrackedTagService trackedTagService, IgnoredTagService ignoredTagService) {
        this.tagDtoService = tagDtoService;
        this.tagService = tagService;
        this.trackedTagService = trackedTagService;
        this.ignoredTagService = ignoredTagService;
    }

    @Autowired
    public TrackedTagMapper trackedTagMapper;

    @Autowired
    public IgnoredTagMapper ignoredTagMapper;

    @GetMapping(path = "/related")
    @Operation(summary = "Get related tags dto", responses = {
            @ApiResponse(description = "Get related tags success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RelatedTagsDto.class)))
    })
    public ResponseEntity<?> getRelatedTagsDto() {
        List<RelatedTagsDto> relatedTags = tagDtoService.getRelatedTagsDto();
        return ResponseEntity.ok(relatedTags);
    }

    @Operation(summary = "Get ignored tags dto", responses = {
            @ApiResponse(description = "Get a list of ignored tags successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDto.class)))
    })
    @GetMapping("/ignored")
    public ResponseEntity<?> getIgnoredTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> ignoredTags = tagDtoService.getIgnoredTagsByUserId(user.getId());
        return ResponseEntity.ok(ignoredTags);
    }

    @GetMapping("/tracked")
    @Operation(summary = "Get tracked tags dto", responses = {
            @ApiResponse(description = "Get a list of tracked tags successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDto.class)))
    })
    public ResponseEntity<?> getAllTrackedTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> trackedTags = tagDtoService.getTrackedTagsByUserId(user.getId());
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
        List<TagDto> tags = tagDtoService.getTagsByLetters(letters);
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
        return ResponseEntity.ok(tagDtoService.getPage(currPage, items, map));
    }

    @Operation(summary = "Get page of tags with pagination sorted by date", responses = {
            @ApiResponse(description = " success", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class)))),
            @ApiResponse(description = "Url parameters are wrong", responseCode = "400")
    })
    @GetMapping("/new")
    public ResponseEntity<?> getPaginationTagsByPersistDate(@RequestParam int currPage, @RequestParam(required = false, defaultValue = "10") int items) {
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "TagPaginationByDate");
        return ResponseEntity.ok(tagDtoService.getPage(currPage, items, map));
    }

    @Operation(summary = "Get all tags with pagination sorted by popularity", responses = {
            @ApiResponse(description = "success", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class)))),
            @ApiResponse(description = "There is no curPage in the url or the parameters in the url are wrong", responseCode = "400")
    })
    @GetMapping("/popular")
    public ResponseEntity<?> getTagPaginationOrderByPopulation(@RequestParam int currPage, @RequestParam(required = false, defaultValue = "10") int items) {
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "TagPaginationOrderByPopulation");
        return ResponseEntity.ok(tagDtoService.getPage(currPage, items, map));
    }

    @Operation(summary = "Add Ignored tag to user", responses = {
            @ApiResponse(description = "successfully added ignored tag", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class))))
    })
    @PostMapping(value = "/{id}/ignored")
    public ResponseEntity<?> addToIgnoredTagsList(@Valid @RequestParam String name, @PathVariable("id") Long id) {
        Optional<Tag> addedTag = tagService.getByName(name);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (addedTag.isEmpty()) {
            return ResponseEntity.badRequest().body("The tag \"" + name + "\" does not exist on this site");
        }
        if (ignoredTagService.getIgnoredTagByName(user.getId(), name).isPresent()) {
            return ResponseEntity.badRequest().body("The ignored tag has already been added");
        }
        IgnoredTag createIgnoredTag = new IgnoredTag();
        createIgnoredTag.setUser(user);
        createIgnoredTag.setIgnoredTag(addedTag.get());
        ignoredTagService.persist(createIgnoredTag);
        IgnoredTagDto createNewTagDto = ignoredTagMapper.persistConvertToDto(createIgnoredTag);
        return ResponseEntity.ok(createNewTagDto);
    }

    @Operation(summary = "Add Tracked tag to user", responses = {
            @ApiResponse(responseCode = "200", description = "successfully added tracked tag",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class))))
    })
    @PostMapping(value = "/{id}/tracked")
    public ResponseEntity<?> addToTrackedTagsList(@Valid @RequestParam String name, @PathVariable("id") Long id) {
        Optional<Tag> addedTag = tagService.getByName(name);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (addedTag.isEmpty()) {
            return ResponseEntity.badRequest().body("The tag \"" + name + "\" does not exist on this site");
        }
        if (trackedTagService.getTrackedTagByName(user.getId(), name).isPresent()) {
            return ResponseEntity.badRequest().body("The tracked tag has already been added");
        }
        TrackedTag createTrackedTag = new TrackedTag();
        createTrackedTag.setUser(user);
        createTrackedTag.setTrackedTag(addedTag.get());
        trackedTagService.persist(createTrackedTag);
        TrackedTagDto createNewTagDto = trackedTagMapper.persistConvertToDto(createTrackedTag);
        return ResponseEntity.ok(createNewTagDto);
    }

}
