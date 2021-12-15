package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.util.InviteService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("api/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping("/{email}")
    @Operation(summary = "Invite user", responses = {
            @ApiResponse(responseCode = "200", description = "User created and invite message sent successfully"),
            @ApiResponse(responseCode = "400", description = "User not created and invite message not sent")})
    public ResponseEntity<?> invite(
            @ApiParam("Email of the user to be invited")
            @NotBlank(message = "Email cannot be empty") @Email @PathVariable final String email) {
        if (inviteService.invite(email)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Cannot invite, because user is already registered");
    }


}
