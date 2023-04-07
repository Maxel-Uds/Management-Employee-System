package com.management.employee.system.service;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.request.CompanyUpdateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import com.management.employee.system.controller.response.CompanyResponse;
import com.management.employee.system.controller.response.CompanyUpdateResponse;
import reactor.core.publisher.Mono;

public interface CompanyService {

    Mono<CompanyCreateResponse> createCompanyAsync(CompanyCreateRequest request);
    Mono<Void> createCompany(CompanyCreateRequest request);
    Mono<Void> deleteCompany(String companyId, TokenAuthentication tokenAuthentication);
    Mono<CompanyResponse> getCompanyById(String companyId);
    Mono<CompanyUpdateResponse> updateCompany(CompanyUpdateRequest companyUpdateRequest, String companyId);
}
