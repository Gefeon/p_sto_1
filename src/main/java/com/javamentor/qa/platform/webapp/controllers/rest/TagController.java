package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.mapper.TagMapper;
import com.javamentor.qa.platform.service.abstracts.model.question.IgnoredTagService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {SwaggerConfig.TAG_CONTROLLER})
@RestController
@RequestMapping("/api/user/tag")
public class TagController {

    private final TagMapper tagMapper;
    private final IgnoredTagService ignoredTagService;
    private final UserDetailsService userDetailsService;

    public TagController(TagMapper tagMapper, IgnoredTagService ignoredTagService, UserDetailsService userDetailsService) {
        this.tagMapper = tagMapper;
        this.ignoredTagService = ignoredTagService;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Get ignored tags from authenticated user", responses = {
            @ApiResponse(description = "tags were retreived from DB", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Tag.class)))
    })

    @GetMapping("/ignored")
    public ResponseEntity<List<TagDto>> getAllIgnoredTags() {
        //TODO сделать чтобы Security загружала UserDetails в Principal
        User user = (User) userDetailsService.loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Tag> ignoredTags = ignoredTagService.getTagsByUser(user);
        return new ResponseEntity<>(tagMapper.toDto(ignoredTags), HttpStatus.OK);
    }
}
