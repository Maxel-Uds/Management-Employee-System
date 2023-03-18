package com.management.employee.system.controller.exceptionHandler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse {

    private List<FieldMessage> errors;

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName, message));
    }
}
