package com.management.employee.system.service.impl;

import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Company;
import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.AuthUserRepository;
import com.management.employee.system.repositories.item.AuthUserItem;
import com.management.employee.system.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final AuthUserRepository authUserRepository;

    @Override
    public Mono<UserDetails> findByUserName(String userName) {
        return authUserRepository.findByUserName(userName)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("Username [%s] not found", userName))))
                .doFirst(() -> log.info("==== Starting find to user with username [{}] ====", userName.replace("(.{5})", "*****")))
                .doFinally(signalType -> log.info("==== End of username search with signal type [{}] ====", signalType));
    }

    @Override
    public Mono<AuthUser> createAuthUser(AuthUserItem authUser) {
        return null;
    }
}
