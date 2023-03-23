package com.management.employee.system.service;

import com.management.employee.system.controller.request.CompanyOwner;
import com.management.employee.system.model.Company;
import com.management.employee.system.model.Owner;
import reactor.core.publisher.Mono;

public interface OwnerService {

    Mono<Owner> saveOwner(CompanyOwner owner);
    Mono<Owner> createOwnerAuthUser(Owner owner, Company company);
}
