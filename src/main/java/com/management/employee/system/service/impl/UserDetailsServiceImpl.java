package com.management.employee.system.service.impl;

import com.management.employee.system.model.AuthUser;
import com.management.employee.system.service.AuthUserService;
import com.management.employee.system.service.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final AuthUserService authUserService;
    private final OwnerService ownerService;
    private final String clientId;

    @Autowired
    public UserDetailsServiceImpl(AuthUserService authUserService,
                                  OwnerService ownerService,
                                  @Value("${app.auth-client.user}") String clientId) {
        this.authUserService = authUserService;
        this.clientId = clientId;
        this.ownerService = ownerService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return authUserService.findByUserName(username)
                .flatMap(userDetails -> this.updateUserScopes((AuthUser) userDetails))
                .doOnError(throwable -> log.error("Invalid Credentials: {}", throwable.getMessage()));
    }

    private Mono<UserDetails> updateUserScopes(AuthUser authUser) {
        if(authUser.getUsername().equals(clientId)) {
            log.info("===== Using application client. Exiting of flux ====");
            return Mono.just(authUser);
        }

        //TODO: Substituir o Mono.empty da linha 50 pela chamada da função da Employee service para formatação dos scopes
        return Mono.just(authUser.getUserType().equals(AuthUser.UserType.ADMIN))
                .flatMap(isAdmin -> isAdmin ? ownerService.formatOwnerScopes(authUser.getPayload().get("companyId")) : Mono.empty())
                .flatMap(scopesFormatedFromTable -> {
                    return !authUser.getScopes().equals(scopesFormatedFromTable) ? this.updateAuthUserScopes(scopesFormatedFromTable, authUser) : Mono.defer(() -> {
                        log.info("===== Scopes of user [{}] already updated ====", authUser.getId());
                        return Mono.just(authUser);
                    });
                });
    }

    private Mono<AuthUser> updateAuthUserScopes(Set<String> scopes, AuthUser authUser) {
        log.info("==== Updating auth user scopes ====");
        return this.authUserService.updateAuthUserScopes(authUser.setScopes(scopes));
    }
}
