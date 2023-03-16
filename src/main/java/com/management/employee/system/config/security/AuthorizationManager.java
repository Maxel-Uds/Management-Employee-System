package com.management.employee.system.config.security;

import com.management.employee.system.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorizationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        var auth = (TokenAuthentication) authentication;
        var bearerToken = auth.getBearerToken().substring("Bearer ".length());
        return Mono.just(bearerToken)
                .then(this.jwtUtil.verifyToken(bearerToken))
                .doOnError(error -> log.error("Invalid Credentials: [{}]", error.getMessage()))
                .onErrorResume(e -> Mono.error(new BadCredentialsException("Invalid Credentials: the token is not valid")))
                .flatMap(decodedJWT -> createAuthenticatedToken(bearerToken));
    }

    private Mono<TokenAuthentication> createAuthenticatedToken(String bearerToken) {
        return Mono.just(new TokenAuthentication(bearerToken));
    }
}
