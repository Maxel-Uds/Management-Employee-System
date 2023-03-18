package com.management.employee.system.controller.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String error;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String path;
}
