package com.management.employee.system.service.impl;

import com.management.employee.system.config.security.TokenAuthentication;
import com.management.employee.system.config.security.scope.Scopes;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class SecurityService {

    public boolean hasDeleteCompanyAccess(TokenAuthentication tokenAuthentication, String companyId) throws AccessDeniedException {
        return tokenAuthentication.getPrincipal().getScopes().contains(Scopes.FULL_ACCESS.name()) ||
               tokenAuthentication.getPrincipal().getScopes().contains(this.formatScope(Scopes.DELETE_COMPANY, companyId));
    }

    private String formatScope(Scopes scope, String id) {
        var key = scope.getScope().split(":")[1];
        return scope.getScope().replace(key, id);
    }
}
