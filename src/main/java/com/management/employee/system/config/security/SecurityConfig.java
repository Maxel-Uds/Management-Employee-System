package com.management.employee.system.config.security;

import com.management.employee.system.config.security.filter.AuthenticationFilter;
import com.management.employee.system.config.security.filter.AuthorizationFilter;
import com.management.employee.system.config.security.filter.ContextPathFilter;
import com.management.employee.system.config.security.handler.AuthenticationFailureHandler;
import com.management.employee.system.config.security.handler.AuthorizationFailureHandler;
import com.management.employee.system.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS_POST = { };

    private final ContextPathFilter contextPathFilter;
    private final AuthenticationFilter authenticationFilter;
    private final AuthorizationFilter authorizationFilter;
    private final ReactiveAuthenticationManager authenticationManager;

    public SecurityConfig(@Value("${server.servlet.context-path}") final String contextPath,
                          @Value("${app.auth.path}") final String applicationLoginPath,
                          final ReactiveAuthenticationManager authenticationManager,
                          final AuthorizationManager authorizationManager,
                          final JwtUtil jwtUtil,
                          final AuthenticationFailureHandler authenticationFailureHandler,
                          final AuthorizationFailureHandler authorizationFailureHandler
                          ) {
        this.authenticationManager = authenticationManager;
        this.contextPathFilter = new ContextPathFilter(contextPath);
        this.authenticationFilter = new AuthenticationFilter(authenticationManager, jwtUtil, contextPath, applicationLoginPath, authenticationFailureHandler);
        this.authorizationFilter = new AuthorizationFilter(authorizationManager, authorizationFailureHandler);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .logout().disable()
                .csrf().disable()
                .authorizeExchange()
                .anyExchange()
                    .authenticated()
                .and()
                    .formLogin()
                    .disable()
                    .authenticationManager(authenticationManager)
                .addFilterAt(this.contextPathFilter, SecurityWebFiltersOrder.FIRST)
                .addFilterAfter(this.authorizationFilter, SecurityWebFiltersOrder.FIRST)
                .addFilterAt(this.authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
