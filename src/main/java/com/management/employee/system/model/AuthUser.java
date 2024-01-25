package com.management.employee.system.model;

import com.management.employee.system.model.enums.UserType;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthUser implements UserDetails {

    private String id;
    private UserType userType;
    @ToString.Exclude
    private String document;
    private String username;
    @ToString.Exclude
    private String password;
    private Set<String> scopes;
    private Map<String, String> payload;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return scopes.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
