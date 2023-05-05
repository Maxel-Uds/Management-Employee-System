package com.management.employee.system.util;

import com.management.employee.system.config.security.TokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TokenUtil {

    public Mono<String> getOwnerId(TokenAuthentication tokenAuthentication) {
        log.info("===== Getting owner id of token payload =====");
        return tokenAuthentication.getPrincipal().getPayload().containsKey("ownerId") ?
                Mono.just(tokenAuthentication.getPrincipal().getPayload().get("ownerId"))
                :
                Mono.error(new AccessDeniedException("Access Denied"));
    }
}
