package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    Mono<String> helloToAuthenticatedUser(TokenAuthentication tokenAuthentication) {
        log.info("Hello user [{}]", tokenAuthentication.getPrincipal().getId());
        return Mono.just("Hello [" + tokenAuthentication.getPrincipal().getId() + "]");
    }
}
