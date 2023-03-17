package com.management.employee.system.service;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface AuthUserService {

    Mono<UserDetails> findByUserName(String userName);
}
