package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class TestRestController {

    @RequestMapping(method = RequestMethod.GET, path = "/user")
    @Operation(summary = "Get user", responses = {
            @ApiResponse(description = "Get user succes", responseCode = "200",
                content = @Content(mediaType = "applocation/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(description = "User not found", responseCode = "409", content = @Content)
    } )
    public ResponseEntity<User> getUser(String id) {
        if ("1".equals(id)) {
            User user = new User();
            user.setId(1L);
            user.setNickname("Vasya");
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Not found");
        }
    }

}
