package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/auth-data")
public class AuthenticationDataController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(T(com.management.employee.system.config.security.scope.Scopes).FULL_ACCESS.getScope())")
    Mono<String> helloToAuthenticatedUser(TokenAuthentication tokenAuthentication) {
        log.info("Hello user [{}]", tokenAuthentication.getPrincipal().getUsername());
        return Mono.just("Hello user [" + tokenAuthentication.getPrincipal().getUsername() + "]");
    }
}
