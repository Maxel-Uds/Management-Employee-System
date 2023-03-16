package com.management.employee.system.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;
import java.util.Objects;

@Getter
public class TokenAuthentication extends AbstractAuthenticationToken  {

    private final String bearerToken;

    public TokenAuthentication(String bearerToken) {
        super(Collections.emptyList());
        this.bearerToken = bearerToken;
        if (bearerToken != null)
            this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return this.getPrincipal();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bearerToken);
    }
}
