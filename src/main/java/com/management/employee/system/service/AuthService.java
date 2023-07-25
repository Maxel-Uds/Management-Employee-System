package com.management.employee.system.service;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.response.TokenResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<TokenResponse> refreshToken(TokenAuthentication tokenAuthentication, ServerHttpRequest request);
    Mono<Void> resetPassword(String ownerEmail);
}
