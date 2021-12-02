package com.javamentor.qa.platform.webapp.controllers.rest;

<<<<<<< src/main/java/com/javamentor/qa/platform/webapp/controllers/rest/UserResourceController.java
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api(tags = {SwaggerConfig.USER_CONTROLLER})
@RestController
@RequestMapping("/api/user")
public class UserResourceController {

    private UserDtoService userDtoService;

    public UserResourceController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping("/{userId}")
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
=======

//ToDo используется как заглушка для тестов

@RestController
@RequestMapping("/api/user")
@Api(tags = {SwaggerConfig.USER_RESOURCE_CONTROLLER})
public class UserResourceController {
    @GetMapping("stub")
    @Operation(summary = "Returns Hello World!")
    public ResponseEntity<?> stub() {
        return ResponseEntity.ok("Hello World!");
>>>>>>> src/main/java/com/javamentor/qa/platform/webapp/controllers/rest/UserResourceController.java
    }
}
