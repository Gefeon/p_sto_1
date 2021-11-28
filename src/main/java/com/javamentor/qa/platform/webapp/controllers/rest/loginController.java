package com.javamentor.qa.platform.webapp.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

// TODO: delete class when real token will be ready
@Getter
@Setter
@AllArgsConstructor
class JwtToken {
    private String token;
}

@Controller
@RequestMapping("/login")
public class loginController {

    @GetMapping
    public String getLoginPage() {
        return "loginPage";
    }

    @ResponseBody
    @PostMapping()
    public ResponseEntity<?> createUser(HttpServletResponse response) {
        return ResponseEntity.ok().body(new JwtToken("dfcdsf435"));
    }
}
