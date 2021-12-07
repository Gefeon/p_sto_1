package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.service.abstracts.dto.RelatedTagsDtoService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = SwaggerConfig.RESOURCE_TAG_CONTROLLER)
@RestController
public class ResourceTagController {

    @Autowired
    private RelatedTagsDtoService relatedTagsDtoService;

    @GetMapping(path = "/api/user/tag/related")
    @Operation(summary = "Get related tags dto", responses = {
            @ApiResponse(description = "Get related tags success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RelatedTagsDto.class))),
            @ApiResponse(description = "No related tags found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<?> getRelatedTagsDto(){
        List<RelatedTagsDto> relatedTags = relatedTagsDtoService.getRelatedTagsDto();
        return relatedTags.isEmpty()
            ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No related tags found")
            : ResponseEntity.ok(relatedTags);
    }
}
