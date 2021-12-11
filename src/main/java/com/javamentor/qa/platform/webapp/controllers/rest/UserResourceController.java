package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Api(tags = {SwaggerConfig.USER_CONTROLLER})
@RestController
public class UserResourceController {

    private UserDtoService userDtoService;
    private PageDtoService<UserDto> pageDtoService;

    public UserResourceController(UserDtoService userDtoService, PageDtoService<UserDto> pageDtoService) {
        this.userDtoService = userDtoService;
        this.pageDtoService = pageDtoService;
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

    @GetMapping(path = "/api/user/reputation")
    @Operation(summary = "Get page pagination users dto reputation", responses = {
            @ApiResponse(description = "Get page dto of users dto success", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDto.class))),
            @ApiResponse(description = "Users not found", responseCode = "404", content = @Content)
    })
    public ResponseEntity<?> getReputation(@RequestParam int currPage, @RequestParam(required = false) Optional<Integer> items) {
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "reputationDto");
        PageDto<UserDto> page = pageDtoService.getPage(currPage,((items.isEmpty() || items.get() == 0) ? 10 : items.get()), map);
        return page.getItems().isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Users")
                : ResponseEntity.ok(page);
    }

//ToDo используется как заглушка для тестов

        @GetMapping("/api/user/stub")
        @Operation(summary = "Returns Hello World!")
        public ResponseEntity<?> stub() {
            return ResponseEntity.ok("Hello World!");

        }
 }
