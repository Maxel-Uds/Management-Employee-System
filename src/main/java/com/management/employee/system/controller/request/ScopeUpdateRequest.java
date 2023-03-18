package com.management.employee.system.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class ScopeUpdateRequest {

    @NotNull
    @NotEmpty(message = "A lista de scopes não pode ser nula")
    private Set<String> scopes;
}
