package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.EmployeeCreateRequest;
import com.management.employee.system.controller.response.EmployeeCreateResponse;
import com.management.employee.system.controller.response.EmployeeResponse;
import com.management.employee.system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@securityService.hasCreateEmployeeAccess(#tokenAuthentication, #request.getCompanyId())")
    Mono<EmployeeCreateResponse> createEmployee(@RequestBody @Valid EmployeeCreateRequest request, TokenAuthentication tokenAuthentication) {
        return employeeService.createEmployee(request)
                .doFirst(() -> log.info("==== Creating employee to company [{}] ====", request.getCompanyId()))
                .doOnSuccess(response -> log.info("==== Employee create with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible create employee. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done create employee process with signal type [{}] ====", signalType));
    }

    @GetMapping("/{employeeId}/company/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@securityService.hasGetEmployeeDataAccess(#tokenAuthentication, #companyId, #employeeId)")
    Mono<EmployeeResponse> getEmployeeDataById(@PathVariable String employeeId, @PathVariable String companyId, TokenAuthentication tokenAuthentication) {
        return Mono.empty();
    }
}
