package com.management.employee.system.controller;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.EmployeeCreateRequest;
import com.management.employee.system.controller.request.EmployeeUpdateRequest;
import com.management.employee.system.controller.response.EmployeeCreateResponse;
import com.management.employee.system.controller.response.EmployeeResponse;
import com.management.employee.system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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
    @PreAuthorize("@securityService.hasGetEmployeeDataAccess(#tokenAuthentication, #companyId)")
    Mono<EmployeeResponse> getEmployeeDataById(@PathVariable String employeeId, @PathVariable String companyId, TokenAuthentication tokenAuthentication) {
        return employeeService.findEmployeeById(companyId, employeeId)
                .doFirst(() -> log.info("==== Getting employee [{}] of company [{}] ====", employeeId, companyId))
                .doOnSuccess(response -> log.info("==== Employee found with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible found employee. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done get employee process with signal type [{}] ====", signalType));
    }

    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@securityService.hasGetSelfEmployeeDataAccess(#tokenAuthentication, #employeeId)")
    Mono<EmployeeResponse> getSelfEmployeeDataById(@PathVariable String employeeId, TokenAuthentication tokenAuthentication) {
        return employeeService.getSelfEmployee(employeeId)
                .doFirst(() -> log.info("==== Getting employee [{}] ====", employeeId))
                .doOnSuccess(response -> log.info("==== Employee found with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible found employee. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done get employee process with signal type [{}] ====", signalType));
    }

    @GetMapping("/get-all-of/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@securityService.hasGetEmployeeDataAccess(#tokenAuthentication, #companyId)")
    Flux<EmployeeResponse> getAllEmployeesByCompanyId(TokenAuthentication tokenAuthentication, @PathVariable String companyId) {
        return employeeService.getAllEmployeesByCompanyId(companyId)
                .doFirst(() -> log.info("==== Getting all employees of company [{}] ====", companyId))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible found employees. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done get employees process with signal type [{}] ====", signalType));
    }

    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@securityService.hasUpdateEmployeeDataAccess(#tokenAuthentication, #employeeId)")
    Mono<EmployeeResponse> updateEmployeeById(TokenAuthentication tokenAuthentication, @PathVariable String employeeId, @RequestBody @Valid EmployeeUpdateRequest request) {
        return employeeService.updateEmployeeDataById(employeeId, request)
                .doFirst(() -> log.info("==== Updating employee [{}] with request [{}] ====", employeeId, request))
                .doOnSuccess(response -> log.info("==== Employee updated with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible update employee. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done update employee process with signal type [{}] ====", signalType));
    }

    @DeleteMapping("/{employeeId}/company/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@securityService.hasDeleteEmployeeAccess(#tokenAuthentication, #companyId)")
    Mono<Void> deleteEmployee(TokenAuthentication tokenAuthentication, @PathVariable String employeeId, @PathVariable String companyId) {
        return employeeService.deleteEmployeeById(employeeId, companyId)
                .doFirst(() -> log.info("==== Deleting employee [{}] ====", employeeId))
                .doOnSuccess(response -> log.info("==== Employee deleted with success ===="))
                .doOnError(throwable -> log.error("==== An error occurred and was not possible delete employee. Error: [{}]", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done delete employee process with signal type [{}] ====", signalType));
    }
}
