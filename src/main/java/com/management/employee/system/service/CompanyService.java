package com.management.employee.system.service;

import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import reactor.core.publisher.Mono;

public interface CompanyService {

    Mono<CompanyCreateResponse> createCompany(CompanyCreateRequest request);
}
