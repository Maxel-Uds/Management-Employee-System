package com.management.employee.system.service.impl;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.request.CompanyUpdateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import com.management.employee.system.controller.response.CompanyResponse;
import com.management.employee.system.controller.response.CompanyUpdateResponse;
import com.management.employee.system.exception.ResourceAlreadyExistsException;
import com.management.employee.system.mapper.CompanyMapper;
import com.management.employee.system.mapper.EmployeeMapper;
import com.management.employee.system.repositories.CompanyRepository;
import com.management.employee.system.repositories.item.CompanyItem;
import com.management.employee.system.service.*;
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

    private final EmailService emailService;
    private final OwnerService ownerService;
    private final CompanyMapper companyMapper;
    private final AuthUserService authUserService;
    private final SqsProducer createCompanyProducer;
    private final SqsProducer deleteEmployeeProducer;
    private final CompanyRepository companyRepository;


    @Override
    public Mono<CompanyCreateResponse> createCompanyAsync(CompanyCreateRequest request) {
        return this.verifyIfAliasExists(request)
                .flatMap(this::verifyIfDocumentExists)
                .map(companyMapper::toEvent)
                .map(this.createCompanyProducer::produce)
                .map(event -> this.companyMapper.toResponse(request));
    }

    @Override
    public Mono<Void> createCompany(CompanyCreateRequest request) {
        log.info("==== Starting process to create a company with request [{}] ====", request);
        return this.ownerService.saveOwner(request.getOwner(), String.format("%s-%s", request.getAlias(), request.getOwner().getOwnerDocument()))
                .zipWhen(owner -> this.companyRepository.save(new CompanyItem(request, owner.getId())))
                .flatMap(ownerAndCompany -> this.ownerService.createOwnerAuthUser(ownerAndCompany.getT1(), ownerAndCompany.getT2()).thenReturn(ownerAndCompany))
                .flatMap(ownerAndCompany -> this.emailService.sendWelcomeMailToOwner(ownerAndCompany.getT1(), ownerAndCompany.getT2()))
                .then();
    }

    @Override
    public Mono<Void> deleteCompany(String companyId, TokenAuthentication tokenAuthentication) {
        return companyRepository.delete(companyId)
                .then(ownerService.deleteOwner(tokenAuthentication.getPrincipal().getPayload().get("ownerId")))
                .then(authUserService.deleteAuthUserByUserName(tokenAuthentication.getPrincipal().getUsername()))
                .thenReturn(companyMapper.toEvent(companyId))
                .map(deleteEmployeeProducer::produce)
                .then(emailService.sendDeletionCompanyEmailToOwner(tokenAuthentication.getPrincipal().getPayload()));
    }

    @Override
    public Mono<CompanyResponse> getCompanyById(String companyId) {
        log.info("==== Getting company [{}] ====", companyId);
        return companyRepository.findById(companyId)
                .flatMap(company -> Mono.just(companyMapper.toResponse(company)));
    }

    @Override
    public Mono<CompanyUpdateResponse> updateCompany(CompanyUpdateRequest request, String companyId) {
        return companyRepository.findById(companyId)
                .flatMap(company -> Mono.just(company.setName(request.getName())))
                .flatMap(company -> companyRepository.updateCompany(new CompanyItem(company)))
                .flatMap(company -> Mono.just(companyMapper.toUpdateResponse(company)));
    }

    private Mono<CompanyCreateRequest> verifyIfAliasExists(CompanyCreateRequest request) {
        log.info("==== Verifing if exists a company with alias [{}] ====", request.getAlias());
        return companyRepository.findByAlias(request.getAlias())
                .flatMap(company -> Objects.nonNull(company.getId()) ? Mono.error(new ResourceAlreadyExistsException("Uma empresa com esse alias já está registrada")) : Mono.just(request));
    }

    private Mono<CompanyCreateRequest> verifyIfDocumentExists(CompanyCreateRequest request) {
        log.info("==== Verifing if exists a company with document [{}] ====", request.getCompanyDocument());
        return companyRepository.findByCNPJ(request.getCompanyDocument())
                .flatMap(company -> Objects.nonNull(company.getId()) ? Mono.error(new ResourceAlreadyExistsException("Uma empresa com esse CNPJ já está registrada")) : Mono.just(request));
    }
}
