package com.management.employee.system.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CompanyCreateRequest {

    @NotEmpty(message = "Nome da empresa é obrigatório")
    private String name;

    @NotEmpty(message = "O alias da empresa é obrigatório")
    @Pattern(regexp="^[a-z0-9]+", message = "O campo alias deve conter apenas letras minúsculas sem acentuação e números")
    @Length(min = 5, max = 20, message = "O alias deve estar entre 5 e 20 caracteres")
    private String alias;

    @Pattern(regexp = "^\\d{14}$", message = "CNPJ inválido")
    @NotEmpty(message = "CNPJ da empresa é obrigatório")
    private String document;

    @Valid
    private CompanyOwner owner;

}
