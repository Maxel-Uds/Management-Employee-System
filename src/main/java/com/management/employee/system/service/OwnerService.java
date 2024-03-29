package com.management.employee.system.service;

import com.management.employee.system.controller.request.CompanyOwner;
import com.management.employee.system.controller.request.OwnerUpdateRequest;
import com.management.employee.system.controller.response.OwnerResponse;
import com.management.employee.system.model.Company;
import com.management.employee.system.model.Owner;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

public interface OwnerService {

    Mono<Owner> saveOwner(CompanyOwner owner, String ownerUserName);
    Mono<Set<String>> formatOwnerScopes(String companyId);
    Mono<Owner> createOwnerAuthUser(Owner owner, Company company);
    Mono<Void> deleteOwner(String ownerId);
    Mono<OwnerResponse> findOwnerById(String ownerId);
    Mono<OwnerResponse> updateOwnerById(String ownerId, OwnerUpdateRequest request);
    Mono<Owner> findOwnerByEmail(String ownerEmail);
    String createRandomPass(Owner owner);
}
