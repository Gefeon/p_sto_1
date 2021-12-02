package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api(tags = {SwaggerConfig.USER_CONTROLLER})
@RestController
public class UserResourceController {

    private UserDtoService userDtoService;

    public UserResourceController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping(path = "/api/user/{userId}")
    @Operation(summary = "Get user dto", responses = {
            @ApiResponse(description = "Get user dto success", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(description = "User not found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<Object> getUserDto(@PathVariable("userId") Long id) {
        Optional<UserDto> dto = userDtoService.getUserDtoById(id);
        return dto.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is absent or wrong Id")
                : ResponseEntity.ok(dto.get());
    }

//ToDo используется как заглушка для тестов

        @GetMapping("stub")
        @Operation(summary = "Returns Hello World!")
        public ResponseEntity<?> stub() {
            return ResponseEntity.ok("Hello World!");

        }
 }
