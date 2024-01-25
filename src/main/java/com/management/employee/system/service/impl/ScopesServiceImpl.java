package com.management.employee.system.service.impl;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.controller.response.ScopeUpdateResponse;
import com.management.employee.system.mapper.ScopesMapper;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Scopes;
import com.management.employee.system.model.enums.UserType;
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
    public Mono<ScopeUpdateResponse> saveScope(UserType role, ScopeUpdateRequest scopeUpdateRequest) {
        log.info("==== Start process of add scope [{}] to [{}] ====", scopeUpdateRequest.getScopes(), role);
        return scopeRepository.addScope(scopeUpdateRequest, role)
                .map(mapper::toResponse);
    }

    @Override
    public Mono<ScopeUpdateResponse> removeScope(UserType role, ScopeUpdateRequest scopeUpdateRequest) {
        log.info("==== Start process of remove scope [{}] of [{}] ====", scopeUpdateRequest.getScopes(), role);
        return scopeRepository.removeScope(scopeUpdateRequest, role)
                .map(mapper::toResponse);
    }

    @Override
    public Mono<Scopes> findByUserType(UserType userType) {
        log.info("==== Getting all scopes of role [{}] ====", userType.name());
        return scopeRepository.findScopesByUserType(userType);
    }
}
