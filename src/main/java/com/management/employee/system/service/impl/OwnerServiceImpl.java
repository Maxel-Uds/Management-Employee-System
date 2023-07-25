package com.management.employee.system.service.impl;

import com.management.employee.system.controller.request.CompanyOwner;
import com.management.employee.system.controller.request.OwnerUpdateRequest;
import com.management.employee.system.controller.response.OwnerResponse;
import com.management.employee.system.mapper.OwnerMapper;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Company;
import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.OwnerRepository;
import com.management.employee.system.repositories.item.AuthUserItem;
import com.management.employee.system.repositories.item.OwnerItem;
import com.management.employee.system.service.AuthUserService;
import com.management.employee.system.service.OwnerService;
import com.management.employee.system.service.ScopesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerMapper ownerMapper;
    private final PasswordEncoder encoder;
    private final ScopesService scopesService;
    private final OwnerRepository ownerRepository;
    private final AuthUserService authUserService;

    @Override
    public Mono<Owner> saveOwner(CompanyOwner owner, String ownerUserName) {
        return this.ownerRepository.save(new OwnerItem(owner, ownerUserName))
                .doFirst(() -> log.info("==== Saving owner [{}] ====", owner))
                .doOnSuccess(resp -> log.info("==== Owner saved with success ===="))
                .doOnError(throwable -> log.error("==== An error ocurred when saving owner. Error: [{}] ====", throwable.getMessage()))
                .doFinally(signalType -> log.info("==== Done save process of owner with signal type [{}] ====", signalType));
    }

    @Override
    public Mono<Owner> createOwnerAuthUser(Owner owner, Company company) {
        log.info("==== Start creation of auth user to owner [{}] ====", owner.getId());
        return this.formatOwnerScopes(company.getId())
                .zipWhen(scopes -> this.createPayload(company, owner))
                .flatMap(scopesAndPayload -> Mono.just(new AuthUserItem(owner, scopesAndPayload.getT1(), createRandomPass(owner), scopesAndPayload.getT2())))
                .flatMap(this.authUserService::createAuthUser)
                .thenReturn(owner);
    }

    @Override
    public Mono<Void> deleteOwner(String ownerId) {
        log.info("==== Deleting owner [{}] ====", ownerId);
        return ownerRepository.delete(ownerId);
    }

    @Override
    public Mono<OwnerResponse> findOwnerById(String ownerId) {
        log.info("==== Getting data of owner [{}] ====", ownerId);
        return ownerRepository.findById(ownerId)
                .map(ownerMapper::toResponse);
    }

    @Override
    public Mono<OwnerResponse> updateOwnerById(String ownerId, OwnerUpdateRequest request) {
        log.info("==== Updating owner [{}] with request [{}] ====", ownerId, request);
        return ownerRepository.findById(ownerId)
                .map(owner -> this.changeOwnerData(owner, request))
                .map(OwnerItem::new)
                .flatMap(ownerRepository::updateOwner)
                .map(ownerMapper::toResponse)
                .flatMap(ownerResponse -> {
                    return authUserService.findByUserName(ownerResponse.getUsername())
                            .map(userDetails -> (AuthUser) userDetails)
                            .map(authUser -> {
                                authUser.getPayload().put("ownerEmail", request.getEmail());
                                authUser.getPayload().put("ownerName", request.getName());
                                return authUser;
                            })
                            .flatMap(authUserService::updateAuthUser)
                            .thenReturn(ownerResponse);
                });
    }

    @Override
    public Mono<Owner> findOwnerByEmail(String ownerEmail) {
        log.info("==== Looking for owner with email [{}] ====", ownerEmail);
        return ownerRepository.findByEmail(ownerEmail);
    }

    @Override
    public Mono<Set<String>> formatOwnerScopes(String companyId) {
        Map<String, String> ids = new HashMap<>() {{
           put("companyId", companyId);
        }};

        return this.scopesService.findByUserType(AuthUser.UserType.ADMIN)
                .flatMap(scopes -> Mono.just(scopes.getScopes().stream().map(scope -> {
                    String key = scope.split(":")[1];
                    return scope.replace(key, ids.get(key));
                }).collect(Collectors.toSet())));
    }

    @Override
    public String createRandomPass(Owner owner) {
        String characters = "!@#$%^&*()_+ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        owner.setPassword(sb.toString());
        return encoder.encode(sb.toString());
    }

    private Owner changeOwnerData(Owner owner, OwnerUpdateRequest request) {
        return owner.setName(request.getName())
                .setPhone(request.getPhone())
                .setEmail(request.getEmail());
    }

    private Mono<Map<String, String>> createPayload(Company company, Owner owner) {
        return Mono.just(new LinkedHashMap<>() {{
            put("companyId", company.getId());
            put("companyAlias", company.getAlias());
            put("ownerId", owner.getId());
            put("ownerEmail", owner.getEmail());
            put("ownerName", owner.getName());
        }});
    }
}
