package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.controller.response.ScopeUpdateResponse;
import com.management.employee.system.service.ScopesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/scopes")
@RequiredArgsConstructor
public class ScopeController {

    private final ScopesService scopesService;

    @PutMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(T(com.management.employee.system.config.security.scope.Scopes).FULL_ACCESS.getScope())")
    Mono<ScopeUpdateResponse> addScopeToRole(@RequestParam(name = "to") String role, @Valid  @RequestBody ScopeUpdateRequest scopeUpdateRequest, TokenAuthentication tokenAuthentication) {
        return scopesService.saveScope(role, scopeUpdateRequest)
                .doFirst(() -> log.info("==== Starting updating scopes of user type [{}]", role))
                .doOnSuccess(scopes -> log.info("==== Scopes of role [{}] updated with success ====", role))
                .doOnError(throwable -> log.error("==== An error ocurred and was not possible update scopes. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done process of update scopes with signal type [{}] ====", signalType));
    }
}
