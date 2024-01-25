package com.management.employee.system.service;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.controller.response.ScopeUpdateResponse;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Scopes;
import com.management.employee.system.model.enums.UserType;
import reactor.core.publisher.Mono;

public interface ScopesService {

    Mono<ScopeUpdateResponse> saveScope(UserType role, ScopeUpdateRequest scopeUpdateRequest);
    Mono<ScopeUpdateResponse> removeScope(UserType role, ScopeUpdateRequest scopeUpdateRequest);
    Mono<Scopes> findByUserType(UserType userType);
}
