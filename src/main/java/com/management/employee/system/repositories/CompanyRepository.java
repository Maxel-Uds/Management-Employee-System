package com.management.employee.system.repositories;

import com.management.employee.system.model.Company;
import reactor.core.publisher.Mono;

public interface CompanyRepository {

    Mono<Company> findByAlias(String alias);
    Mono<Company> findByCNPJ(String cnpj);
}
