package com.management.employee.system.service;

import com.management.employee.system.model.Company;
import com.management.employee.system.model.Owner;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface EmailService {

    Mono<Void> sendWelcomeMailToOwner(Owner owner, Company company);
    Mono<Void> sendDeletionCompanyEmailToOwner(Map<String, String> authUserPayload);
}
