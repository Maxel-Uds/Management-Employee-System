package com.management.employee.system.controller.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CompanyResponse {

    private String id;
    private String name;
    private String alias;
    private String companyDocument;
    private String ownerId;
}
