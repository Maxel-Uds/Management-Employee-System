package com.management.employee.system.controller.exceptionHandler;

import com.management.employee.system.exception.ResourceAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ErrorResponse validationExceptionHanlder(WebExchangeBindException exception){

        final List<FieldMessage> errors =
                exception.getFieldErrors()
                        .stream()
                        .map(fieldError ->
                                FieldMessage.builder()
                                        .fieldName(fieldError.getField())
                                        .fieldError(fieldError.getDefaultMessage())
                                        .build())
                        .collect(Collectors.toList());

        log.error("==== Input error ====");
        return ValidationErrorResponse
                .builder()
                .errors(errors)
                .timestamp(System.currentTimeMillis())
                .message("Input validation error")
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse runtimeExceptionHandler(RuntimeException exception, ServerHttpRequest request){
        return badRequestResponseOf(exception, request);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse accessDeniedException(AccessDeniedException exception, ServerHttpRequest request){
        return forbiddenRequestResponseOf(exception, request);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ErrorResponse resourceAlreadyExistsException(ResourceAlreadyExistsException exception, ServerHttpRequest request){
        return dataConflictRequestResponseOf(exception, request);
    }

    private ErrorResponse dataConflictRequestResponseOf(RuntimeException exception, ServerHttpRequest request){
        log.error("==== Error: [{}]. Path: [{}]. Method: [{}]. Code: [{}] ====", exception.getMessage(), request.getURI().getPath(), request.getMethod(), HttpStatus.CONFLICT.value());
        return ErrorResponse
                .builder()
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getURI().getPath())
                .error(HttpStatus.CONFLICT.name().toLowerCase())
                .build();
    }

    private ErrorResponse badRequestResponseOf(RuntimeException exception, ServerHttpRequest request){
        log.error("==== Error: [{}]. Path: [{}]. Method: [{}]. Code: [{}] ====", exception.getMessage(), request.getURI().getPath(), request.getMethod(), HttpStatus.BAD_REQUEST.value());
        return ErrorResponse
                .builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getURI().getPath())
                .error(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .build();
    }

    private ErrorResponse forbiddenRequestResponseOf(AccessDeniedException exception, ServerHttpRequest request) {
        log.error("==== Error: [{}]. Path: [{}]. Method: [{}]. Code: [{}] ====", exception.getMessage(), request.getURI().getPath(), request.getMethod(), HttpStatus.FORBIDDEN.value());
        return ErrorResponse
                .builder()
                .message(exception.getMessage() + ": you don't have sufficient permission to access this endpoint.")
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(System.currentTimeMillis())
                .path(request.getURI().getPath())
                .error(HttpStatus.FORBIDDEN.name().toLowerCase())
                .build();
    }
}
