package com.management.employee.system.service;

import com.management.employee.system.controller.request.EmployeeCreateRequest;
import com.management.employee.system.controller.response.EmployeeCreateResponse;
import com.management.employee.system.controller.response.EmployeeResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface EmployeeService {

    Mono<EmployeeCreateResponse> createEmployee(EmployeeCreateRequest request);
    Mono<Set<String>> formatEmployeeScopes(String companyId, String employeeId);
    Mono<EmployeeResponse> findEmployeeById(String companyId, String employeeId);
    Mono<EmployeeResponse> getSelfEmployee(String employeeId);
    Flux<EmployeeResponse> getAllEmployeesByCompanyId(String companyId);
}
