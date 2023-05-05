package com.management.employee.system.service.impl;

import com.google.gson.Gson;
import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.response.TokenResponse;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.repositories.RefreshTokenRepository;
import com.management.employee.system.repositories.item.RefreshTokenItem;
import com.management.employee.system.service.AuthService;
import com.management.employee.system.service.AuthUserService;
import com.management.employee.system.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import org.springframework.security.access.AccessDeniedException;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthUserService authUserService;

    @Override
    public Mono<TokenResponse> refreshToken(TokenAuthentication tokenAuthentication, ServerHttpRequest request) {
        return this.verifyIfRefreshTokenIsUsed(tokenAuthentication.getBearerToken())
                .flatMap(refreshTokenItem -> {
                    return authUserService.findByUserName(refreshTokenItem.getUsername())
                            .doOnSuccess(userDetails -> log.info("==== Refreshing token of auth user [{}] ====", ((AuthUser) userDetails).getId()))
                            .flatMap(userDetails -> jwtUtil.createTokens((AuthUser) userDetails, request))
                            .flatMap(token -> refreshTokenRepository.saveRefreshToken(refreshTokenItem)
                                    .thenReturn(new Gson().fromJson(token, TokenResponse.class)));
                });
    }

    private Mono<RefreshTokenItem> verifyIfRefreshTokenIsUsed(String token) {
        log.info("==== Verifying if refresh token already used ====");
        return this.refreshTokenRepository.getRefreshToken(token)
                .flatMap(refreshTokenItem -> Objects.nonNull(refreshTokenItem.getToken()) ? Mono.error(new AccessDeniedException("This refresh token is not valid anymore")) : this.createRefreshTokenItem(token));
    }

    private Mono<RefreshTokenItem> createRefreshTokenItem(String token) {
        return this.jwtUtil.verifyToken(token)
                .map(decodedJWT -> new RefreshTokenItem(token, decodedJWT.getClaim("exp").asLong(), decodedJWT.getClaim("sub").asString()));
    }
}
