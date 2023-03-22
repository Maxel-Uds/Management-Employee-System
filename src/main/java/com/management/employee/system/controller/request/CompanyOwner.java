package com.management.employee.system.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.management.employee.system.model.AuthUser;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Accessors(chain = true)
public class CompanyOwner {

    @NotEmpty(message = "O nome do owner é obrigatório")
    private String name;

    @NotEmpty(message = "O número do telefone é obrigatório!")
    @Pattern(regexp="\\[1-9]{2}9[1-9][0-9]{3}[0-9]{4}", message = "Digite  DD e um número de telefone válido no formato!")
    private String phone;

    @Email
    @NotEmpty(message = "O email é obrigatório!")
    private String email;

    @Pattern(regexp = "\\d{11}", message = "CPF inválido")
    @NotEmpty(message = "O documento é obrigatório!")
    private String document;

    @JsonIgnore
    private AuthUser.UserType userType = AuthUser.UserType.ADMIN;

}
