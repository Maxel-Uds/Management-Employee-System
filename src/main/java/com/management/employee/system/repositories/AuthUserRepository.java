package com.management.employee.system.repositories;

import com.management.employee.system.model.AuthUser;
import com.management.employee.system.repositories.item.AuthUserItem;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface AuthUserRepository {

    Mono<AuthUser> save(AuthUserItem authUser);
    Mono<UserDetails> findByUserName(String userName);
    Mono<AuthUser> updateAuthUser(AuthUser authUser);
    Mono<Void> delete(String authUserId);

}
