package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.webapp.configs.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//ToDo используется как заглушка для тестов

@RestController
@RequestMapping("/api/user")
@Api(tags = {SwaggerConfig.USER_RESOURCE_CONTROLLER})
public class UserResourceController {
    @GetMapping("stub")
    @Operation(summary = "Returns Hello World!")
    public ResponseEntity<?> stub() {
        return ResponseEntity.ok("Hello World!");
    }
}
