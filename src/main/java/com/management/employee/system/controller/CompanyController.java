package com.management.employee.system.controller;

import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import com.management.employee.system.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<CompanyCreateResponse> createCompany(@RequestBody @Valid CompanyCreateRequest request) {
        return companyService.createCompany(request)
                .doFirst(() -> log.info("==== Starting process of create company ===="))
                .doOnSuccess(response -> log.info("==== Company created with success ===="))
                .doOnError(throwable -> log.error("==== An error ocurred and was not possible create company. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done creation company process with signal type [{}] ====", signalType));
    }
}
