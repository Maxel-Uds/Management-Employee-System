package com.management.employee.system.controller.request;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Accessors(chain = true)
public class UpdatePasswordRequest {

    @Length(min = 5, max = 15, message = "A senha deve estar entre 5 e 15 caracteres")
    @NotEmpty(message = "A senha do funcionário não pode ser nula")
    private String password;
}
