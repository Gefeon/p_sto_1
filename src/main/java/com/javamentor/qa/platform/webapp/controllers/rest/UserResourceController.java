package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Api(tags = {SwaggerConfig.USER_CONTROLLER})
@RestController
@Validated
public class UserResourceController {

    private final UserDtoService userDtoService;

    public UserResourceController(UserDtoService userDtoService) {
            this.userDtoService = userDtoService;
        }

        @GetMapping(path = "/api/user/{userId}")
        @Operation(summary = "Get user dto", responses = {
                @ApiResponse(description = "Get user dto success", responseCode = "200",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                @ApiResponse(description = "User not found", responseCode = "404")
        })
        public ResponseEntity<Object> getUserDto(@PathVariable("userId") Long id) {
            Optional<UserDto> dto = userDtoService.getUserDtoById(id);
            return dto.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is absent or wrong Id")
                    : ResponseEntity.ok(dto.get());
        }

        @GetMapping(path = "/api/user/new")
        @Operation(summary = "Get page with pagination by users' persist datetime", responses = {
                @ApiResponse(description = " success", responseCode = "200",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                        @ApiResponse(description = "there isn`t curPage parameter in url or parameters in url are not positives numbers", responseCode = "400")
        })
        public ResponseEntity<?> getPageDtoByUserPersistDate(
                @ApiParam(value = "positive number representing number of current page", required = true)
                @RequestParam @Positive(message = "current page must be positive number") int currPage,
                @ApiParam(value = "positive number representing number of items to show on page")
                @RequestParam(required = false, defaultValue = "10") @Positive(message = "items must be positive number") int items) {
            Map<Object, Object> map = new HashMap<>();
            map.put("class", "paginationByPersistDate");
            PageDto<UserDto> page = userDtoService.getPage(currPage, items, map);
            return ResponseEntity.ok(page);
        }

//ToDo используется как заглушка для тестов

        @GetMapping("/api/user/stub")
        @Operation(summary = "Returns Hello World!")
        public ResponseEntity<?> stub() {
            return ResponseEntity.ok("Hello World!");

        }
    }