package com.javamentor.qa.platform.webapp.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexey Achkasov
 * @version 1.0, 29.11.2021
 */
@RestController
@RequestMapping("/api/user")
public class UserResourceController {
    @GetMapping("stub")
    public ResponseEntity<?> stub(Authentication auth) {
        return ResponseEntity.ok("Hello " + auth.getName());
    }
}
