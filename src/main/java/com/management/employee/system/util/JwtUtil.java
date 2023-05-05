package com.management.employee.system.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.employee.system.model.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Algorithm algorithm;
    private final Long refreshTokenExpiration;
    private final Long accessTokenExpiration;

    public JwtUtil(@Value("${app.token.refresh-token-exp}") Long refreshTokenExpiration,
                   @Value("${app.token.access-token-exp}") Long accessTokenExpiration,
                   @Value("${app.token.jwt-secret}") String jwtSecret) {
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.accessTokenExpiration = accessTokenExpiration;
        this.algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
    }

    public Mono<String> createTokens(AuthUser user, ServerHttpRequest request){
        String jwtId = UUID.randomUUID().toString();
        String accessToken = createAccessToken(request.getURI().toString(), user, jwtId);
        String refreshToken = createRefreshToken(request.getURI().toString(), user);

        Map<String, Object> token = new LinkedHashMap<>() {{
            put("payload", user.getPayload());
            put("scopes", user.getScopes());
            put("access_token", accessToken);
            put("refresh_token", refreshToken);
            put("token_type", "Bearer");
            put("jti", jwtId);
        }};
        token.put("expires_in", this.accessTokenExpiration);

        try {
            String body = new ObjectMapper().writeValueAsString(token);
            return Mono.just(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Mono.empty();
        }
    }

    private String createAccessToken(String requestPath, AuthUser user, String jwtId) {
        return JWT.create()
                .withJWTId(jwtId)
                .withClaim("payload", user.getPayload())
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(requestPath)
                .withClaim("scope", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }

    private String createRefreshToken(String requestPath, AuthUser user) {
        return JWT.create()
                .withClaim("payload", user.getPayload())
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("userId", user.getId())
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(requestPath)
                .sign(algorithm);
    }

    public Mono<DecodedJWT> verifyToken(String token){
        return Mono.just(algorithm)
                .map(alg -> JWT.require(alg).build())
                .map(verifier -> verifier.verify(token));
    }
}
