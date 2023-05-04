package com.management.employee.system.sqs.event;

import com.management.employee.system.controller.request.CompanyOwner;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompanyCreateEvent extends Event {

    private String name;
    private String alias;
    private String companyDocument;
    private CompanyOwner owner;
}
