package com.management.employee.system.repositories;

import com.management.employee.system.model.Company;
import com.management.employee.system.repositories.item.CompanyItem;
import reactor.core.publisher.Mono;

public interface CompanyRepository {

    Mono<Company> findByAlias(String alias);
    Mono<Company> findByCNPJ(String cnpj);
    Mono<Company> save(CompanyItem company);
    Mono<Void> delete(String companyId);
    Mono<Company> findById(String companyId);
    Mono<Company> updateCompany(CompanyItem companyItem);
}
