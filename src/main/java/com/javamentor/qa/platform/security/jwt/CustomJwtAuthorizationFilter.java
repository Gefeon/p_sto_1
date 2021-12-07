package com.javamentor.qa.platform.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomJwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/api/auth/token");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<DecodedJWT> optionalDecodedJWT = jwtService.processToken(request);
        if (optionalDecodedJWT.isPresent()) {
            String email = optionalDecodedJWT.get().getSubject();
            String role = optionalDecodedJWT.get().getClaim("role").asString();
            UserDetails userDetails = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

/*
передавай в UsernamePasswordAuthenticationToken не email а user,
collections.singleton(new SimpleGrantedAuthority(role) и вот это не нужно просто передавай объект role


* */