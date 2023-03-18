package com.management.employee.system.repositories;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Scopes;
import reactor.core.publisher.Mono;

public interface ScopeRepository {
    Mono<Scopes> saveScope(ScopeUpdateRequest request, AuthUser.UserType userType);

}
