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
        this.scopes.remove("");
        return this;
    }

    public Scopes removeScope(Set<String> scopes) {
        if(!this.scopes.contains("")) {
            this.scopes.removeAll(scopes);
        }

        this.scopes.add("");
        return this;
    }
}
