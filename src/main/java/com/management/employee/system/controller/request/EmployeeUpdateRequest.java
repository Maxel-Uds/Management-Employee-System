package com.management.employee.system.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EmployeeUpdateRequest {

    @Length(min = 5, max = 20, message = "O nome deve estar entre 5 e 20 caracteres")
    @NotEmpty(message = "O Nome do funcionário é obrigatório")
    private String name;

    @Email
    @NotEmpty(message = "O email do funcionário é obrigatório")
    private String email;
}
