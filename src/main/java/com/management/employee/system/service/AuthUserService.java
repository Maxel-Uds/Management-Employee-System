package com.management.employee.system.service;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Company;
import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.item.AuthUserItem;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface AuthUserService {

    Mono<UserDetails> findByUserName(String userName);
    public Mono<AuthUser> createAuthUser(AuthUserItem authUser);
}
