package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.UpdatePasswordRequest;
import com.management.employee.system.controller.response.TokenResponse;
import com.management.employee.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/reset/password/{ownerEmail}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> resetOwnerPassword(@PathVariable String ownerEmail) {
        return authService.resetOwnerPassword(ownerEmail)
                .doFirst(() -> log.info("==== Starting process of reseting password ===="))
                .doOnSuccess(response -> log.info("==== Process done with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible reset password. Error: [{}] ====", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done password reset process with signal type [{}] ====", signalType));
    }

    @PutMapping("/reset/password/employee/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@securityService.hasResetPasswordEmployeeAccess(#tokenAuthentication)")
    Mono<Void> resetEmployeePassword(@PathVariable String employeeId, @RequestBody UpdatePasswordRequest request, TokenAuthentication tokenAuthentication) {
        final String companyId = tokenAuthentication.getPrincipal().getPayload().get("companyId");

        return authService.resetEmployeePassword(employeeId, request, companyId)
                .doFirst(() -> log.info("==== Starting process of reseting employee password ===="))
                .doOnSuccess(response -> log.info("==== Process done with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible reset employee password. Error: [{}] ====", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done employee password reset process with signal type [{}] ====", signalType));
    }
}
