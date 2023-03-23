package com.management.employee.system.service;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.controller.response.ScopeUpdateResponse;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Scopes;
import reactor.core.publisher.Mono;

public interface ScopesService {

    Mono<ScopeUpdateResponse> saveScope(String role, ScopeUpdateRequest scopeUpdateRequest);
    Mono<ScopeUpdateResponse> removeScope(String role, ScopeUpdateRequest scopeUpdateRequest);
    Mono<Scopes> findByUserType(AuthUser.UserType userType);
}
