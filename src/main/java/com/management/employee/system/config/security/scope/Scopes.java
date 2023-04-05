package com.management.employee.system.config.security.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Scopes {

    FULL_ACCESS("fullAccess:application"),
    DELETE_COMPANY("delete:companyId:company"),
    GET_COMPANY_DATA("getData:companyId:company");

    private final String scope;
}
