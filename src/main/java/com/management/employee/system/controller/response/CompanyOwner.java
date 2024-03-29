package com.management.employee.system.controller.response;

import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.enums.UserType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CompanyOwner {

    private String name;
    private String phone;
    private String email;
    private String ownerDocument;
    private UserType userType;
}
