package com.javamentor.qa.platform.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Alexey Achkasov
 * @version 1.0, 25.11.2021
 */
@Getter
@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.expiration_period}")
    private Long accessTokenValidTime;

    private Algorithm algorithm;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public String createAccessToken(String username, String role) {
        Long currentTime = System.currentTimeMillis();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(getIssuedAt(currentTime))
                .withExpiresAt(getExpiresAt(currentTime, accessTokenValidTime))
                .withClaim("role", role)
                .sign(algorithm);
    }

    public Optional<DecodedJWT> processToken(HttpServletRequest request) throws JWTVerificationException {
        String rawToken = request.getHeader(AUTHORIZATION);
        if (rawToken != null && rawToken.startsWith("Bearer ")) {
            String token = rawToken.substring(7);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return Optional.of(verifier.verify(token));
        } else {
            return Optional.empty();
        }
    }

    private Date getExpiresAt(Long currentTime, Long validTime) {
        return new Date(currentTime + validTime);
    }

    private Date getIssuedAt(Long currentTime) {
        return new Date(currentTime);
    }
}
