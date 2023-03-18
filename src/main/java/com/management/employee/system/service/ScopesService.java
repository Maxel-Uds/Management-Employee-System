package com.management.employee.system.service;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.controller.response.ScopeUpdateResponse;
import reactor.core.publisher.Mono;

public interface ScopesService {

    Mono<ScopeUpdateResponse> saveScope(String role, ScopeUpdateRequest scopeUpdateRequest);
}
