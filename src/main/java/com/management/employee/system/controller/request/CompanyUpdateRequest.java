package com.management.employee.system.controller.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class CompanyUpdateRequest {

    @NotEmpty(message = "Nome da empresa é obrigatório")
    private String name;
}
