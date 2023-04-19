package com.management.employee.system.service.impl;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.config.security.scope.Scopes;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Service
public class SecurityService {

    public boolean hasGetEmployeeDataAccess(TokenAuthentication tokenAuthentication, String companyId, String employeeId) {
        return Objects.nonNull(tokenAuthentication.getPrincipal().getPayload().get("ownerId")) ?
                tokenAuthentication.getPrincipal().getScopes().contains(this.formatScope(Scopes.OWNER_GET_EMPLOYEE_ACCESS, companyId)) :
                tokenAuthentication.getPrincipal().getScopes().contains(this.formatScope(Scopes.GET_EMPLOYEE_ACCESS, employeeId));
    }

    public boolean hasCreateEmployeeAccess(TokenAuthentication tokenAuthentication, String companyId) {
        return tokenAuthentication.getPrincipal().getScopes().contains(this.formatScope(Scopes.CREATE_EMPLOYEE_ACCESS, companyId));
    }

    public boolean hasDeleteCompanyAccess(TokenAuthentication tokenAuthentication, String companyId) throws AccessDeniedException {
        return tokenAuthentication.getPrincipal().getScopes().contains(this.formatScope(Scopes.DELETE_COMPANY, companyId));
    }

    public boolean hasGetCompanyAccess(TokenAuthentication tokenAuthentication, String companyId) throws AccessDeniedException {
        return tokenAuthentication.getPrincipal().getScopes().contains(this.formatScope(Scopes.GET_COMPANY_DATA, companyId));
    }

    public boolean hasUpdateCompanyAccess(TokenAuthentication tokenAuthentication, String companyId) throws AccessDeniedException {
        return tokenAuthentication.getPrincipal().getScopes().contains(this.formatScope(Scopes.UPDATE_COMPANY_DATA, companyId));
    }

    private String formatScope(Scopes scope, String id) {
        var key = scope.getScope().split(":")[1];
        return scope.getScope().replace(key, id);
    }
}
