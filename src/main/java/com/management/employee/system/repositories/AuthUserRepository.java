package com.management.employee.system.repositories;

import com.management.employee.system.model.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface AuthUserRepository {

    Mono<AuthUser> save();
    Mono<UserDetails> findByUserName(String userName);
}
