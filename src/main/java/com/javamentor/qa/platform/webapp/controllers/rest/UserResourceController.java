package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserReputationDtoService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import com.javamentor.qa.platform.service.impl.dto.UserReputationDtoServiceImpl;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Api(tags = {SwaggerConfig.USER_CONTROLLER})
@RestController
@Validated
public class UserResourceController {

    private final UserDtoService userDtoService;
    private final UserReputationDtoService userReputationDtoService;
    private final UserService userService;

    public UserResourceController(UserDtoService userDtoService, UserReputationDtoService userReputationDtoService, UserService userService) {
        this.userDtoService = userDtoService;
        this.userReputationDtoService = userReputationDtoService;
        this.userService = userService;
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

    @GetMapping(path = "/api/user/reputation")
    @Operation(summary = "Get page pagination users dto reputation", responses = {
            @ApiResponse(description = "Get page dto of users dto success", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageDto.class))),
            @ApiResponse(description = "Wrong parameters current page or items", responseCode = "400", content = @Content)
    })
    public ResponseEntity<?> getReputation(@RequestParam int currPage, @RequestParam(required = false, defaultValue = "10") int items) {
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "UserReputation");
        return ResponseEntity.ok(userReputationDtoService.getPage(currPage, items, map));
    }

    @Operation(summary = "change user password", responses = {
            @ApiResponse(description = "Password was changed", responseCode = "200"),
            @ApiResponse(description = "Password not changed", responseCode = "400")
    })
    @PutMapping("api/user/changePassword")
    public ResponseEntity<?> changePassword(
            @ApiParam(value = "Password of the user to be changed", required = true)
            @NotBlank(message = "Password cannot be empty") @RequestBody final String password,
            Authentication authentication) {

        boolean onlyLatinAlphabet = password.matches("^[a-zA-Z0-9!@#$%^&*()-=+|\\\\,.:;~_<>?{}\\[\\]\"']+$");

        if (password.length() < 6 || password.length() > 12) {
            return new ResponseEntity<>("Length of password from 6 to 12 symbols", HttpStatus.BAD_REQUEST);
        }

        if (!onlyLatinAlphabet) {
            return new ResponseEntity<>("Use only latin alphabet, numbers and special chars", HttpStatus.BAD_REQUEST);
        }

        Long id = ((User) authentication.getPrincipal()).getId();
        userService.changePasswordById(id, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(path = "/api/user/vote")
    @Operation(summary = "Get page pagination users dto by vote", responses = {
            @ApiResponse(description = "success", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(description = "Wrong parameters current page or items", responseCode = "400", content = @Content)
    })
    public ResponseEntity<?> getPaginationByVote(@RequestParam int currPage, @RequestParam(required = false, defaultValue = "10") int items) {
        Map<Object, Object> map = new HashMap<>();
        map.put("class", "UserPaginationByVote");
        return ResponseEntity.ok(userDtoService.getPage(currPage, items, map));
    }

//ToDo используется как заглушка для тестов

    @GetMapping("/api/user/stub")
    @Operation(summary = "Returns Hello World!")
    public ResponseEntity<?> stub() {
        return ResponseEntity.ok("Hello World!");

    }
}
