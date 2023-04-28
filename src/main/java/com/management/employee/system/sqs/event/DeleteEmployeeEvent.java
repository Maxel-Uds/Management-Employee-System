package com.management.employee.system.sqs.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteEmployeeEvent extends Event {

    private String companyId;
}
