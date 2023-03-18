package com.management.employee.system.config.security.filter;

import com.management.employee.system.config.security.handler.AuthenticationFailureHandler;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.*;

@Slf4j
public class AuthenticationFilter extends AuthenticationWebFilter  {

    private final JwtUtil jwtUtil;
    private final String contextPath;
    private final String applicationLoginPath;

    public AuthenticationFilter(ReactiveAuthenticationManager authenticationManager,
                                JwtUtil jwtUtil,
                                String contextPath,
                                String applicationLoginPath,
                                AuthenticationFailureHandler authenticationFailureHandler
    ) {
        super(authenticationManager);
        setRequiresAuthenticationMatcher(this::matchAuthenticationHeader);
        setRequiresAuthenticationMatcher(this::matchLoginPath);
        setRequiresAuthenticationMatcher(this::matchLoginMethod);
        setAuthenticationFailureHandler(authenticationFailureHandler);
        this.jwtUtil = jwtUtil;
        this.contextPath = contextPath;
        this.applicationLoginPath = applicationLoginPath;
    }

    protected final Mono<ServerWebExchangeMatcher.MatchResult> matchLoginMethod(ServerWebExchange exchange) {
        log.trace("========= AuthenticationFilter matching login method ===========");
        return Mono.fromCallable(() -> exchange.getRequest().getHeaders())
                .filter(httpHeaders -> httpHeaders.containsKey(AUTHORIZATION))
                .filter(httpHeaders -> StringUtils.startsWithIgnoreCase(httpHeaders.getFirst(AUTHORIZATION), "basic "))
                .flatMap(httpHeaders -> Objects.equals(exchange.getRequest().getMethod(), HttpMethod.POST) ? ServerWebExchangeMatcher.MatchResult.match() : ServerWebExchangeMatcher.MatchResult.notMatch()).switchIfEmpty(ServerWebExchangeMatcher.MatchResult.match());
    }

    protected final Mono<ServerWebExchangeMatcher.MatchResult> matchLoginPath(ServerWebExchange exchange) {
        log.trace("========= AuthenticationFilter matching login path ===========");
        return Mono.fromCallable(() -> exchange.getRequest().getHeaders())
                .filter(httpHeaders -> httpHeaders.containsKey(AUTHORIZATION))
                .filter(httpHeaders -> StringUtils.startsWithIgnoreCase(httpHeaders.getFirst(AUTHORIZATION), "basic "))
                .flatMap(httpHeaders -> {
                    var loginPath = this.getLoginPath(exchange.getRequest().getURI());
                    var userAgent = httpHeaders.getFirst(USER_AGENT);
                    var host = httpHeaders.getFirst(HOST);

                    if(!loginPath.startsWith(this.applicationLoginPath)) {
                        log.error("==== Path [{}] does not match login path [{}]. Host [{}] User Agent [{}] ====", loginPath, applicationLoginPath, host, userAgent);
                        return ServerWebExchangeMatcher.MatchResult.notMatch();
                    }

                    return ServerWebExchangeMatcher.MatchResult.match();
                }).switchIfEmpty(ServerWebExchangeMatcher.MatchResult.match());
    }

    protected final Mono<ServerWebExchangeMatcher.MatchResult> matchAuthenticationHeader(ServerWebExchange exchange){
        log.trace("========= AuthenticationFilter matching authentication ===========");
        return Mono.fromCallable(() -> exchange.getRequest().getHeaders())
                .filter(httpHeaders -> httpHeaders.containsKey(AUTHORIZATION))
                .flatMap(result -> {
                    String authorization = result.getFirst(AUTHORIZATION);
                    if(StringUtils.startsWithIgnoreCase(authorization, "basic"))
                        return ServerWebExchangeMatcher.MatchResult.match();
                    else
                        return ServerWebExchangeMatcher.MatchResult.notMatch();
                })
                .switchIfEmpty(ServerWebExchangeMatcher.MatchResult.notMatch());
    }

    @Override
    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        var response = webFilterExchange.getExchange().getResponse();
        var request = webFilterExchange.getExchange().getRequest();

        return this.jwtUtil.createTokens(user, request)
                .switchIfEmpty(Mono.error(new RuntimeException("Could not create JWT tokens")))
                .map(token -> token.getBytes(StandardCharsets.UTF_8))
                .map(bytes -> response.bufferFactory().wrap(bytes))
                .flatMap(buffer -> {
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return response.writeWith(Mono.just(buffer));
                }).then();
    }

    private String getLoginPath(URI uri) {
        return uri.getPath().replace(contextPath, "");
    }

}
