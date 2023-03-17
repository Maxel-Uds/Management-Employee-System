package com.management.employee.system.service.impl;

import com.management.employee.system.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final AuthUserService authUserService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return authUserService.findByUserName(username)
                .doOnError(throwable -> log.error("Invalid Credentials: {}", throwable.getMessage()));
    }
}
