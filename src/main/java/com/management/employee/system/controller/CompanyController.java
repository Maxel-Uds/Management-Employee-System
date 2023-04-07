package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.request.CompanyUpdateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import com.management.employee.system.controller.response.CompanyResponse;
import com.management.employee.system.controller.response.CompanyUpdateResponse;
import com.management.employee.system.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
        return companyService.createCompanyAsync(request)
                .doFirst(() -> log.info("==== Starting process of create company ===="))
                .doOnSuccess(response -> log.info("==== Company created with success ===="))
                .doOnError(throwable -> log.error("==== An error ocurred and was not possible create company. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done creation company process with signal type [{}] ====", signalType));
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@securityService.hasDeleteCompanyAccess(#tokenAuthentication, #companyId)")
    Mono<Void> deleteCompany(@PathVariable String companyId, TokenAuthentication tokenAuthentication) {
        return companyService.deleteCompany(companyId, tokenAuthentication)
                .doFirst(() -> log.info("==== Starting process of delete company [{}] ====", companyId))
                .doOnSuccess(response -> log.info("==== Company deleted with success ===="))
                .doOnError(throwable -> log.error("==== An error ocurred and was not possible delete company. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done deletion company process with signal type [{}] ====", signalType));
    }

    @GetMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@securityService.hasGetCompanyAccess(#tokenAuthentication, #companyId)")
    Mono<CompanyResponse> getCompany(@PathVariable String companyId, TokenAuthentication tokenAuthentication) {
        return companyService.getCompanyById(companyId)
                .doFirst(() -> log.info("==== Looking for company [{}] ====", companyId))
                .doOnSuccess(response -> log.info("==== Company found with success ===="))
                .doOnError(throwable -> log.error("==== An error ocurred and was not possible get company. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done search company process with signal type [{}] ====", signalType));
    }

    @PatchMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@securityService.hasUpdateCompanyAccess(#tokenAuthentication, #companyId)")
    Mono<CompanyUpdateResponse> updateCompany(@RequestBody CompanyUpdateRequest request, @PathVariable String companyId, TokenAuthentication tokenAuthentication) {
        return companyService.updateCompany(request, companyId)
                .doFirst(() -> log.info("==== Updating company [{}] ====", companyId))
                .doOnSuccess(response -> log.info("==== Company updated with success ===="))
                .doOnError(throwable -> log.error("==== An error ocurred and was not possible update company. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done update company process with signal type [{}] ====", signalType));
    }
}
