package com.management.employee.system.model;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Employee {

    private String id;
    private String name;
    private String email;
    private String document;
    private String username;
    private String companyId;
    @ToString.Exclude
    private String password;
}
