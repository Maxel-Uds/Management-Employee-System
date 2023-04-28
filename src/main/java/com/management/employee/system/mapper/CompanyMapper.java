package com.management.employee.system.mapper;

import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.controller.response.CompanyCreateResponse;
import com.management.employee.system.controller.response.CompanyResponse;
import com.management.employee.system.controller.response.CompanyUpdateResponse;
import com.management.employee.system.model.Company;
import com.management.employee.system.sqs.event.CompanyCreateEvent;
import com.management.employee.system.sqs.event.DeleteEmployeeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyCreateResponse toResponse(CompanyCreateRequest request);
    CompanyResponse toResponse(Company company);
    CompanyUpdateResponse toUpdateResponse(Company company);
    CompanyCreateEvent toEvent(CompanyCreateRequest request);
    @Mapping(target = "companyId", source = "companyId")
    DeleteEmployeeEvent toEvent(String companyId);
}
