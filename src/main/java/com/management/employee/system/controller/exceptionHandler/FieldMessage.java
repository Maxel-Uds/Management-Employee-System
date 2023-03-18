package com.management.employee.system.controller.exceptionHandler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldMessage {
    private final String fieldName;
    private final String fieldError;
}
