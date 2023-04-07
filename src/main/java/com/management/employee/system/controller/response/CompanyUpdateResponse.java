package com.management.employee.system.controller.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CompanyUpdateResponse {

    private String name;
}
