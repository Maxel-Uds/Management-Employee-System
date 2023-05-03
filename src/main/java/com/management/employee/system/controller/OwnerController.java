package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.response.OwnerResponse;
import com.management.employee.system.service.OwnerService;
import com.management.employee.system.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final TokenUtil tokenUtil;
    private final OwnerService ownerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<OwnerResponse> getOwnerData(TokenAuthentication tokenAuthentication) {
        return tokenUtil.getOwnerId(tokenAuthentication)
                .flatMap(ownerService::findOwnerById)
                .doFirst(() -> log.info("==== Looking for owner by id ===="))
                .doOnSuccess(response -> log.info("==== Owner found with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible find owner. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done find owner process with signal type [{}] ====", signalType));
    }
}
