package com.management.employee.system.service.impl;

import com.management.employee.system.model.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return createStandartUser()
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("Username [%s] not found", username))))
                .doOnError(throwable -> log.error("Invalid Credentials: {}", throwable.getMessage()));
    }

    private Mono<UserDetails> empty() {
        return Mono.empty();
    }

    private Mono<UserDetails> createStandartUser() {
        var id = UUID.randomUUID().toString();

        Map<String, Object> payload = new LinkedHashMap<>() {{
            put("id", id);
            put("email", "email.teste@gmail.com");
            put("phone", "419981205500");
            put("name", "Maxel Udson Alves");
        }};

        var pass = passwordEncoder.encode("1234567");
        return Mono.just(AuthUser.builder()
                .id(id)
                .username("max1234")
                .name("Maxel")
                .password(pass)
                .payload(payload)
                .scopes(Set.of("add:all", "delete:self", "create:user", "update:user"))
                .build());
    }
}
