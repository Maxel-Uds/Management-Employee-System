package com.management.employee.system.service.impl;

import com.google.gson.Gson;
import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.UpdatePasswordRequest;
import com.management.employee.system.controller.response.TokenResponse;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.RefreshTokenRepository;
import com.management.employee.system.repositories.item.RefreshTokenItem;
import com.management.employee.system.service.*;
import com.management.employee.system.util.JwtUtil;
import com.management.employee.system.util.PasswordUtil;
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
    private final PasswordUtil passwordUtil;
    private final EmailService emailService;
    private final OwnerService ownerService;
    private final EmployeeService employeeService;
    private final AuthUserService authUserService;
    private final RefreshTokenRepository refreshTokenRepository;

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

    @Override
    public Mono<Void> resetOwnerPassword(String ownerEmail) {
        return ownerService.findOwnerByEmail(ownerEmail)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("==== Not found any owner with email [{}] ====", ownerEmail);
                    return Mono.empty();
                }))
                .zipWhen(owner -> authUserService.findByUserName(owner.getUsername()))
                .flatMap(ownerAndUserDetails -> {
                    return Mono.just(ownerService.createRandomPass(ownerAndUserDetails.getT1()))
                            .map(encodedPass -> ((AuthUser) ownerAndUserDetails.getT2()).setPassword(encodedPass))
                            .flatMap(authUserService::updateAuthUser)
                            .flatMap(authUser -> emailService.sendResetPasswordEmail(ownerAndUserDetails.getT1()));
                });
    }

    @Override
    public Mono<Void> resetEmployeePassword(String employeeId, UpdatePasswordRequest request, String companyId) {
        return employeeService.findEmployeeById(companyId, employeeId)
                .flatMap(employeeResponse -> authUserService.findByUserName(employeeResponse.getUsername())
                        .map(userDetails -> (AuthUser) userDetails)
                )
                .flatMap(authUser -> this.changeEmployeeAuthUserPass(request, authUser))
                .flatMap(authUserService::updateAuthUser)
                .then();
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

    private Mono<AuthUser> changeEmployeeAuthUserPass(UpdatePasswordRequest request, AuthUser authUser) {
        return Mono.just(authUser.setPassword(passwordUtil.encryptPass(request.getPassword())));
    }
}
