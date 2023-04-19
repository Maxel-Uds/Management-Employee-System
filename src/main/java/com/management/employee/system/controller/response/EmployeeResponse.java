package com.management.employee.system.controller.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmployeeResponse {

    private String id;
    private String name;
    private String email;
    private String document;
    private String username;
    private String companyId;
}
