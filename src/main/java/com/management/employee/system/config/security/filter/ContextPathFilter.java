package com.management.employee.system.config.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Slf4j
@RequiredArgsConstructor
public class ContextPathFilter implements WebFilter {

    private final String context;


    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        log.trace("==== ContextPathFilter --> Checking context path! ==== ");
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith(context)) {
            log.trace("==== ContextPathFilter --> Path {} is OK ====", path);
            return chain.filter(addContextPath(exchange));
        } else {
            // if do not start with the context we block it
            log.error("==== Path [{}] does not have the context path [{}] ====", path, context);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Path not found");
        }
    }

    private ServerWebExchange addContextPath(ServerWebExchange exchange) {
        return exchange.mutate().request(exchange.getRequest().mutate().contextPath(context).build()).build();
    }
}
