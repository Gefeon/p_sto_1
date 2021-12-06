package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.models.dto.TokenResponseDto;
import com.javamentor.qa.platform.security.jwt.JwtService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth/token")
@RequiredArgsConstructor
@Api(tags = {SwaggerConfig.AUTHENTICATION_CONTROLLER})
public class AuthenticationResourceController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping
    @Operation(summary = "Authenticate user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful authentication"),
            @ApiResponse(responseCode = "400", description = "Invalid user credentials")})
    public ResponseEntity<TokenResponseDto> createToken(
            @ApiParam(value = "A JSON object containing user login and password", required = true)
            @Valid @RequestBody final AuthenticationRequestDto authDto) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
        TokenResponseDto token = jwtService.createAccessToken(authDto.getUsername(),
                auth.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .getAuthority());
        return ResponseEntity.ok(token);
    }
}
