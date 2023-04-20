package com.management.employee.system.mapper;

import com.management.employee.system.controller.response.EmployeeCreateResponse;
import com.management.employee.system.controller.response.EmployeeResponse;
import com.management.employee.system.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeCreateResponse toEmployeeCreateResponse(Employee employee);
    EmployeeResponse toEmployeeResponse(Employee employee);
}
