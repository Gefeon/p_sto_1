package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDto;
import com.javamentor.qa.platform.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Achkasov
 * @version 1.0, 25.11.2021
 */
@RestController
@RequestMapping("/api/auth/token")
@RequiredArgsConstructor
public class AuthenticationResourceController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        String username = authenticationRequestDto.getUsername();
        String password = authenticationRequestDto.getPassword();
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        //throws AuthenticationException if bad credentials is present
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtService.createAccessToken(username, auth.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow()
                .getAuthority());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
