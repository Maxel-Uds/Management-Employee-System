package com.management.employee.system.config.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.employee.system.controller.exceptionHandler.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        try {
            var errorResponse = new ObjectMapper().writeValueAsString(buildErrorResponse(webFilterExchange.getExchange().getRequest())).getBytes(StandardCharsets.UTF_8);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ErrorResponse buildErrorResponse(ServerHttpRequest request) {
        return ErrorResponse.builder()
                .message("Invalid Credentials: username or password are incorrect")
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getURI().getPath())
                .error(HttpStatus.UNAUTHORIZED.name().toLowerCase())
                .build();
    }
}
