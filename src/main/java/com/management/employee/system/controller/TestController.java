package com.management.employee.system.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @PostMapping("/no-auth")
    @ResponseStatus(HttpStatus.CREATED)
    private Mono<String> noAuth() {
        log.warn("noAuth");
        return Mono.just("NoAuth");
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    private Mono<String> auth() {
        log.warn("auth");
        return Mono.just("Auth");
    }
}
