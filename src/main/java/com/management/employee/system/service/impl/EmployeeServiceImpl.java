package com.management.employee.system.service.impl;

import com.management.employee.system.controller.request.EmployeeCreateRequest;
import com.management.employee.system.controller.request.EmployeeUpdateRequest;
import com.management.employee.system.controller.response.CompanyResponse;
import com.management.employee.system.controller.response.EmployeeCreateResponse;
import com.management.employee.system.controller.response.EmployeeResponse;
import com.management.employee.system.exception.ResourceAlreadyExistsException;
import com.management.employee.system.exception.ResourceNotFoundException;
import com.management.employee.system.mapper.EmployeeMapper;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Employee;
import com.management.employee.system.repositories.EmployeeRepository;
import com.management.employee.system.repositories.item.AuthUserItem;
import com.management.employee.system.repositories.item.EmployeeItem;
import com.management.employee.system.service.AuthUserService;
import com.management.employee.system.service.CompanyService;
import com.management.employee.system.service.EmployeeService;
import com.management.employee.system.service.ScopesService;
import com.management.employee.system.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final PasswordUtil passwordUtil;
    private final ScopesService scopesService;
    private final CompanyService companyService;
    private final EmployeeMapper employeeMapper;
    private final AuthUserService authUserService;
    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeCreateResponse> createEmployee(EmployeeCreateRequest request) {
        return companyService.getCompanyById(request.getCompanyId())
                .flatMap(companyResponse -> this.checkIfUserAlreadyExists(companyResponse, request))
                .zipWhen(company -> employeeRepository.save(new EmployeeItem(request, String.format("%s-%s", company.getAlias(), request.getDocument()))))
                .flatMap(employeeAndCompany -> Mono.just(employeeAndCompany.getT2().setPassword(request.getPassword())).thenReturn(employeeAndCompany))
                .flatMap(employeeAndCompany -> this.createEmployeeAuthUser(employeeAndCompany.getT2(), employeeAndCompany.getT1()))
                .flatMap(employee -> Mono.just(employeeMapper.toEmployeeCreateResponse(employee)));
    }

    @Override
    public Mono<EmployeeResponse> findEmployeeById(String companyId, String employeeId) {
        log.info("=== Looking for employee with id [{}] ====", employeeId);
        return employeeRepository.getAllEmployeesByCompanyId(companyId)
                .filter(employee -> employee.getId().equals(employeeId))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("=== Not found any employee with id [{}] ====", employeeId);
                    return Mono.error(new ResourceNotFoundException("Nenhum funcionário foi encontrado com o id informado"));
                }))
                .last()
                .flatMap(employee -> Mono.just(employeeMapper.toEmployeeResponse(employee)));
    }

    @Override
    public Mono<Set<String>> formatEmployeeScopes(String companyId, String employeeId) {
        Map<String, String> ids = new HashMap<>() {{
            put("companyId", companyId);
            put("employeeId", employeeId);
        }};

        return this.scopesService.findByUserType(AuthUser.UserType.EMPLOYEE)
                .flatMap(scopes -> Mono.just(scopes.getScopes().stream().map(scope -> {
                    String key = scope.split(":")[1];
                    return scope.replace(key, ids.get(key));
                }).collect(Collectors.toSet())));
    }

    @Override
    public Mono<EmployeeResponse> getSelfEmployee(String employeeId) {
        log.info("==== Looking for employee with id [{}] ====", employeeId);
        return this.employeeRepository.findById(employeeId)
                .flatMap(employee -> Mono.just(employeeMapper.toEmployeeResponse(employee)));
    }

    @Override
    public Flux<EmployeeResponse> getAllEmployeesByCompanyId(String companyId) {
        log.info("==== Looking for employees of company [{}] ====", companyId);
        return employeeRepository.getAllEmployeesByCompanyId(companyId)
                .map(employeeMapper::toEmployeeResponse);
    }

    @Override
    public Mono<EmployeeResponse> updateEmployeeDataById(String employeeId, EmployeeUpdateRequest request) {
        log.info("==== Updating employee [{}] ====", employeeId);
        return employeeRepository.findById(employeeId)
                .map(employee -> employee.setName(request.getName()).setEmail(request.getEmail()))
                .map(EmployeeItem::new)
                .flatMap(employeeRepository::update)
                .map(employeeMapper::toEmployeeResponse)
                .flatMap(employeeResponse -> {
                    return authUserService.findByUserName(employeeResponse.getUsername())
                                    .map(userDetails -> (AuthUser) userDetails)
                                    .map(authUser -> {
                                        authUser.getPayload().put("employeeEmail", request.getEmail());
                                        authUser.getPayload().put("employeeName", request.getName());
                                        return authUser;
                                    })
                                    .flatMap(authUserService::updateAuthUser)
                                    .thenReturn(employeeResponse);
                });
    }

    @Override
    public Mono<Void> deleteEmployeeById(String employeeId, String companyId) {
        log.info("==== Deleting employee [{}] of company [{}] ====", employeeId, companyId);
        return this.findEmployeeById(companyId, employeeId)
                .flatMap(employeeResponse -> employeeRepository.delete(employeeId).thenReturn(employeeResponse))
                .flatMap(employeeResponse -> authUserService.deleteAuthUserByUserName(employeeResponse.getUsername()));
    }

    @Override
    public Mono<Void> deleteEmployeeAsync(EmployeeResponse response) {
        log.info("==== Deleting employee async with id [{}] ====", response.getId());
        return employeeRepository.delete(response.getId());
    }

    private Mono<CompanyResponse> checkIfUserAlreadyExists(CompanyResponse company, EmployeeCreateRequest request) {
        return authUserService.checkIfUserExistsByUsername(String.format("%s-%s", company.getAlias(), request.getDocument()))
                .flatMap(isUserExists -> isUserExists ? Mono.error(new ResourceAlreadyExistsException("Este usuário já pertence a essa empresa")) : Mono.just(company));
    }

    private Mono<Employee> createEmployeeAuthUser(Employee employee, CompanyResponse company) {
        log.info("==== Start creation of auth user to employee [{}] ====", employee.getId());
        return this.formatEmployeeScopes(company.getId(), employee.getId())
                .zipWhen(scopes -> this.createPayload(company, employee))
                .flatMap(scopesAndPayload -> Mono.just(new AuthUserItem(employee.setUsername(String.format("%s-%s", company.getAlias(), employee.getDocument())), scopesAndPayload.getT1(), passwordUtil.encryptPass(employee.getPassword()), scopesAndPayload.getT2())))
                .flatMap(this.authUserService::createAuthUser)
                .thenReturn(employee);
    }

    private Mono<Map<String, String>> createPayload(CompanyResponse company, Employee employee) {
        return Mono.just(new LinkedHashMap<>() {{
            put("companyId", company.getId());
            put("companyAlias", company.getAlias());
            put("employeeId", employee.getId());
            put("employeeEmail", employee.getEmail());
            put("employeeName", employee.getName());
        }});
    }
}
