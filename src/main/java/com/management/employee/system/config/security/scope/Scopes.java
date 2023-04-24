package com.management.employee.system.config.security.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Scopes {

    FULL_ACCESS("fullAccess:application"),
    DELETE_COMPANY("delete:companyId:company"),
    GET_COMPANY_DATA("getData:companyId:company"),
    UPDATE_COMPANY_DATA("updateData:companyId:company"),
    CREATE_EMPLOYEE_ACCESS("createData:companyId:employee"),
    OWNER_GET_EMPLOYEE_ACCESS("getEmployeeData:companyId:company"),
    GET_SELF_EMPLOYEE_ACCESS("getData:employeeId:employee");

    private final String scope;
}
