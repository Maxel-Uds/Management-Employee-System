package com.management.employee.system.repositories;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Scopes;
import com.management.employee.system.model.enums.UserType;
import reactor.core.publisher.Mono;

public interface ScopeRepository {
    Mono<Scopes> addScope(ScopeUpdateRequest request, UserType userType);
    Mono<Scopes> removeScope(ScopeUpdateRequest request, UserType userType);
    Mono<Scopes> findScopesByUserType(UserType userType);

}
