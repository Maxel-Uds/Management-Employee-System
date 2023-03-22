package com.management.employee.system.service.impl;

import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import com.management.employee.system.exception.ResourceAlreadyExistsException;
import com.management.employee.system.mapper.CompanyMapper;
import com.management.employee.system.repositories.CompanyRepository;
import com.management.employee.system.service.CompanyService;
import com.management.employee.system.sqs.SqsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final SqsProducer sqsProducer;

    private final CompanyMapper companyMapper;

    @Override
    public Mono<CompanyCreateResponse> createCompany(CompanyCreateRequest request) {
        return this.verifyIfAliasExists(request)
                .flatMap(this::verifyIfDocumentExists)
                .flatMap(this.sqsProducer::produce)
                .flatMap(companyRequest -> Mono.just(this.companyMapper.toResponse(companyRequest)));

    }

    private Mono<CompanyCreateRequest> verifyIfAliasExists(CompanyCreateRequest request) {
        log.info("==== Verifing if exists a company with alias [{}] ====", request.getAlias());
        return companyRepository.findByAlias(request.getAlias())
                .flatMap(company -> Objects.nonNull(company.getId()) ? Mono.error(new ResourceAlreadyExistsException("Uma empresa com esse alias já está registrada")) : Mono.just(request));
    }

    private Mono<CompanyCreateRequest> verifyIfDocumentExists(CompanyCreateRequest request) {
        log.info("==== Verifing if exists a company with document [{}] ====", request.getDocument());
        return companyRepository.findByCNPJ(request.getDocument())
                .flatMap(company -> Objects.nonNull(company.getId()) ? Mono.error(new ResourceAlreadyExistsException("Uma empresa com esse CNPJ já está registrada")) : Mono.just(request));
    }
}
