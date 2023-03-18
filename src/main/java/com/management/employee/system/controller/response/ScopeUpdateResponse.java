package com.management.employee.system.controller.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class ScopeUpdateResponse {

    private Set<String> scopes;
}
