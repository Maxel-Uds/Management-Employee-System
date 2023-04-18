package com.management.employee.system.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EmployeeCreateRequest {

    @Length(min = 5, max = 20, message = "O nome deve estar entre 5 e 20 caracteres")
    @NotEmpty(message = "O Nome do funcionário é obrigatório")
    private String name;

    @Email
    @NotEmpty(message = "O email do funcionário é obrigatório")
    private String email;

    @Pattern(regexp = "\\d{11}", message = "CPF inválido")
    @NotEmpty(message = "O documento do funcionário é obrigatório")
    private String document;

    @NotEmpty(message = "O id da empresa do funcionário é obrigatório")
    private String companyId;

    @Length(min = 5, max = 15, message = "A senha deve estar entre 5 e 15 caracteres")
    @NotEmpty(message = "A senha do funcionário não pode ser nula")
    private String password;
}
