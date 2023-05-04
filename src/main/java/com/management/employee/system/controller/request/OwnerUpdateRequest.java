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
public class OwnerUpdateRequest {

    @Length(min = 5, max = 20, message = "O nome deve estar entre 5 e 20 caracteres")
    @NotEmpty(message = "O nome do owner é obrigatório")
    private String name;

    @NotEmpty(message = "O número do telefone é obrigatório!")
    @Pattern(regexp="[1-9]{2}9[1-9][0-9]{7}", message = "Digite  DD e um número de telefone válido no formato!")
    private String phone;

    @Email
    @NotEmpty(message = "O email é obrigatório!")
    private String email;
}
