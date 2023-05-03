package com.management.employee.system.controller.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OwnerResponse {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String document;
}
