package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api(tags = {SwaggerConfig.USER_CONTROLLER})
@RestController
public class TestRestController {

    @GetMapping(path = "/user/{id}")
    @Operation(summary = "Get user", responses = {
            @ApiResponse(description = "Get user success", responseCode = "200",
                    content = @Content(mediaType = "applocation/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(description = "User not found", responseCode = "409", content = @Content)
    })
    public ResponseEntity<User> getUser(@ApiParam(value = "The Id of the user", required = true) @PathVariable String id) {
        if ("1".equals(id)) {
            User user = new User();
            user.setId(1L);
            user.setNickname("Vasya");
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not found");
        }
    }

}
