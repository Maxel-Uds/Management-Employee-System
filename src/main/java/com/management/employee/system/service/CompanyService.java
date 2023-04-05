package com.management.employee.system.service;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import reactor.core.publisher.Mono;

public interface CompanyService {

    Mono<CompanyCreateResponse> createCompanyAsync(CompanyCreateRequest request);
    Mono<Void> createCompany(CompanyCreateRequest request);
    Mono<Void> deleteCompany(String companyId, TokenAuthentication tokenAuthentication);
}
