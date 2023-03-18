package com.management.employee.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Scopes {
    private Set<String> scopes;

    public Scopes addScope(Set<String> scopes) {
        if(Objects.isNull(this.scopes))
            this.scopes = new LinkedHashSet<>();
        this.scopes.addAll(scopes);
        return this;
    }
}
