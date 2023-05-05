package com.management.employee.system.controller.response;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class TokenResponse {

    private Map<String, Object> payload;
    private Set<String> scopes;
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String jti;
    private Long expires_in;
}
