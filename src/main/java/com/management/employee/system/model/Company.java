package com.management.employee.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Company {

    private String id;
    private String name;
    private String alias;
    private String companyDocument;
    private String ownerId;
}
