package com.management.employee.system.service.impl;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.controller.response.ScopeUpdateResponse;
import com.management.employee.system.mapper.ScopesMapper;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.repositories.ScopeRepository;
import com.management.employee.system.service.ScopesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScopesServiceImpl implements ScopesService {

    private final ScopeRepository scopeRepository;
    private final ScopesMapper mapper;

    @Override
    public Mono<ScopeUpdateResponse> saveScope(String role, ScopeUpdateRequest scopeUpdateRequest) {
        log.info("==== Start process of add scope [{}] to [{}] ====", scopeUpdateRequest.getScopes(), role);
        return Mono.just(role.toUpperCase().equals(AuthUser.UserType.ADMIN.name()) || role.toUpperCase().equals(AuthUser.UserType.EMPLOYEE.name()))
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Role [{}] does not exists", role.toUpperCase());
                    return Mono.error(new RuntimeException(String.format("Role [%s] does not exists", role.toUpperCase())));
                }))
                .flatMap(aBoolean -> scopeRepository.saveScope(scopeUpdateRequest, AuthUser.UserType.valueOf(role.toUpperCase())))
                .flatMap(scopes -> Mono.just(mapper.toResponse(scopes)));
    }
}
