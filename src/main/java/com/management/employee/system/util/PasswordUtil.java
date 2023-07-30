package com.management.employee.system.util;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordUtil {

    private final PasswordEncoder encoder;

    public String encryptPass(String password) {
        return encoder.encode(password);
    }
}
