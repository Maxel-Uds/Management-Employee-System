package com.management.employee.system.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.management.employee.system.model.AuthUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashMap;
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

        this.authUser = Objects.nonNull(decodedJWT.getClaim("payload").asMap().get("ownerId")) ? this.buildOwnerAuthUser(decodedJWT) : buildEmployeeAuthUser(decodedJWT);
    }

    public TokenAuthentication(String bearerToken) {
        super(null);
        this.bearerToken = bearerToken;
        this.authUser = AuthUser.builder().build();
    }

    @Override
    public Object getCredentials() {
        return this.bearerToken;
    }

    @Override
    public AuthUser getPrincipal() {
        return this.authUser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bearerToken);
    }

    private AuthUser buildOwnerAuthUser(DecodedJWT decodedJWT) {
        return AuthUser.builder()
                .username(decodedJWT.getClaim("sub").asString())
                .scopes(Arrays.stream(decodedJWT.getClaim("scope").asArray(String.class)).collect(Collectors.toSet()))
                .payload(new HashMap<>() {{
                    put("companyAlias", (String) decodedJWT.getClaim("payload").asMap().get("companyAlias"));
                    put("companyId", (String) decodedJWT.getClaim("payload").asMap().get("companyId"));
                    put("ownerId", (String) decodedJWT.getClaim("payload").asMap().get("ownerId"));
                    put("ownerEmail", (String) decodedJWT.getClaim("payload").asMap().get("ownerEmail"));
                    put("ownerName", (String) decodedJWT.getClaim("payload").asMap().get("ownerName"));
                }})
                .build();
    }

    private AuthUser buildEmployeeAuthUser(DecodedJWT decodedJWT) {
        return AuthUser.builder()
                .username(decodedJWT.getClaim("sub").asString())
                .scopes(Arrays.stream(decodedJWT.getClaim("scope").asArray(String.class)).collect(Collectors.toSet()))
                .payload(new HashMap<>() {{
                    put("companyAlias", (String) decodedJWT.getClaim("payload").asMap().get("companyAlias"));
                    put("companyId", (String) decodedJWT.getClaim("payload").asMap().get("companyId"));
                    put("employeeId", (String) decodedJWT.getClaim("payload").asMap().get("employeeId"));
                    put("employeeEmail", (String) decodedJWT.getClaim("payload").asMap().get("employeeEmail"));
                    put("employeeName", (String) decodedJWT.getClaim("payload").asMap().get("employeeName"));
                }})
                .build();
    }
}
