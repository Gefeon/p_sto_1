package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Api(tags = {SwaggerConfig.CHANGE_PASSWORD_CONTROLLER})
@RestController
@RequestMapping("/api/user")
public class PasswordChangeController {

    private UserService userService;

    public PasswordChangeController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "change user password", responses = {
            @ApiResponse(description = "Password was changed", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(description = "Password not changed", responseCode = "400")
    })
    @PutMapping("/changePassword")
    public ResponseEntity<?> addQuestion(
            @ApiParam(value = "Password of the user to be changed", required = true)
            @NotBlank(message = "Password cannot be empty") @RequestParam final String password,
            Authentication authentication){

        boolean onlyLatinAlphabet = password.matches("^[a-zA-Z0-9]+$");

        if (password.length() < 6 || password.length() > 12) {
            return new ResponseEntity<>("Length of password from 6 to 12 symbols", HttpStatus.BAD_REQUEST);
        }

        if (!onlyLatinAlphabet) {
            return new ResponseEntity<>("Use only latin alphabet and numbers", HttpStatus.BAD_REQUEST);
        }

        Long id = ((User) authentication.getPrincipal()).getId();
        userService.changePasswordById(id, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
