package com.management.employee.system.controller.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CompanyCreateResponse {

    private String name;
    private String alias;
    private String companyDocument;
    private CompanyOwner owner;
}
