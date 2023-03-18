package com.management.employee.system.config.security.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Scopes {

    FULL_ACCESS("fullAccess:application");

    private final String scope;
}
