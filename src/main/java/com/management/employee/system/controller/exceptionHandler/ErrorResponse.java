package com.management.employee.system.controller.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class ErrorResponse {

    private final Long timestamp;
    private final Integer status;
    private final String message;
    private final String error;
    private final String path;
}
