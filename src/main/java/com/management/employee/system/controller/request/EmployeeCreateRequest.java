package com.management.employee.system.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EmployeeCreateRequest {

    @NotEmpty(message = "O Nome do funcionário é obrigatório")
    private String name;

    @NotEmpty(message = "O email do funcionário é obrigatório")
    private String email;

    @Pattern(regexp = "\\d{11}", message = "CPF inválido")
    @NotEmpty(message = "O documento do funcionário é obrigatório")
    private String document;

    @NotEmpty(message = "O id da empresa do funcionário é obrigatório")
    private String companyId;

    @NotEmpty(message = "A senha do funcionário não pode ser nula")
    private String password;
}
