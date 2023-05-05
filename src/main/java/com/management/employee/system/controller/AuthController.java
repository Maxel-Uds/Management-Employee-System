package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.response.TokenResponse;
import com.management.employee.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    Mono<TokenResponse> refreshToken(TokenAuthentication tokenAuthentication, ServerHttpRequest request) {
        return authService.refreshToken(tokenAuthentication, request)
                .doFirst(() -> log.info("==== Starting process of refresh token ===="))
                .doOnSuccess(response -> log.info("==== Token refreshed with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible refresh token. Error: [{}] ====", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done refresh token process with signal type [{}] ====", signalType));
    }
}
