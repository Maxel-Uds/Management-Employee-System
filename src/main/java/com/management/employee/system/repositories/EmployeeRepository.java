package com.management.employee.system.repositories;

import com.management.employee.system.model.Employee;
import com.management.employee.system.repositories.item.EmployeeItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeRepository {

    Mono<Employee> save(EmployeeItem employeeItem);
    Flux<Employee> getAllEmployeesByCompanyId(String companyId);
    Mono<Employee> findById(String employeeId);
}
