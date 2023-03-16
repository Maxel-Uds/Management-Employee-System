package com.management.employee.system.config.security.filter;

import com.management.employee.system.config.security.AuthorizationManager;
import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.config.security.handler.AuthorizationFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class AuthorizationFilter extends AuthenticationWebFilter {

    public AuthorizationFilter(AuthorizationManager authenticationManager,
                               AuthorizationFailureHandler authorizationFailureHandler
    ) {
        super(authenticationManager);
        setAuthenticationFailureHandler(authorizationFailureHandler);
        setRequiresAuthenticationMatcher(this::matchAutorizationHeader);
        setServerAuthenticationConverter(this::convertAuthenticationToToken);
    }

    protected final Mono<ServerWebExchangeMatcher.MatchResult> matchAutorizationHeader(ServerWebExchange exchange){
        log.trace("========= AuthorizationFilter matching authentication ===========");
        return Mono.fromCallable(() -> exchange.getRequest().getHeaders())
                .filter(httpHeaders -> httpHeaders.containsKey(AUTHORIZATION))
                .flatMap(result -> {
                    String authorization = result.getFirst(AUTHORIZATION);
                    if(StringUtils.startsWithIgnoreCase(authorization, "bearer"))
                        return ServerWebExchangeMatcher.MatchResult.match();
                    else
                        return ServerWebExchangeMatcher.MatchResult.notMatch();
                })
                .switchIfEmpty(ServerWebExchangeMatcher.MatchResult.notMatch());
    }

    protected final Mono<Authentication> convertAuthenticationToToken(ServerWebExchange exchange){
        log.trace("========= AuthorizationFilter converting authentication ===========");
        return Mono.fromCallable(() -> exchange.getRequest().getHeaders().getFirst(AUTHORIZATION))
                .map(TokenAuthentication::new);
    }
}
