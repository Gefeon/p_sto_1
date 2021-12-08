package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.IgnoredTagDtoService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {SwaggerConfig.TAG_CONTROLLER})
@RestController
@RequestMapping("/api/user/tag")
public class TagController {

    private final IgnoredTagDtoService ignoredTagService;

    public TagController(IgnoredTagDtoService ignoredTagService) {
        this.ignoredTagService = ignoredTagService;
    }

    @Operation(summary = "Get ignored tags from authenticated user", responses = {
            @ApiResponse(description = "tags were retrieved from DB", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Tag.class)))
    })

    @GetMapping("/ignored")
    public ResponseEntity<List<TagDto>> getAllIgnoredTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TagDto> ignoredTags = ignoredTagService.getTagsByUser(user);
        if(!ignoredTags.isEmpty()) {
            ignoredTags.forEach(tag -> tag.setDescription(null));
        }
        return new ResponseEntity<>(ignoredTags, HttpStatus.OK);
    }
}
