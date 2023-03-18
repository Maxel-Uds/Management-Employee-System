package com.management.employee.system.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.management.employee.system.model.AuthUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class TokenAuthentication extends AbstractAuthenticationToken  {
    private final AuthUser authUser;
    private final String bearerToken;

    public TokenAuthentication(String bearerToken, DecodedJWT decodedJWT, String[] scope) {
        super(scope != null ? Arrays.stream(scope).map(SimpleGrantedAuthority::new).
                collect(Collectors.toList()) : null);

        this.bearerToken = bearerToken;
        if (bearerToken != null)
            this.setAuthenticated(true);

        this.authUser = AuthUser.builder()
                .id((String) decodedJWT.getClaim("payload").asMap().get("id"))
                .username(decodedJWT.getClaim("sub").asString())
                .document((String) decodedJWT.getClaim("payload").asMap().get("document"))
                .userType(AuthUser.UserType.valueOf((String) decodedJWT.getClaim("payload").asMap().get("userType")))
                .build();
    }

    public TokenAuthentication(String bearerToken) {
        super(null);
        this.bearerToken = bearerToken;
        this.authUser = AuthUser.builder().build();
    }

    @Override
    public Object getCredentials() {
        return this.getCredentials();
    }

    @Override
    public AuthUser getPrincipal() {
        return this.authUser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bearerToken);
    }
}
