package com.management.employee.system.mapper;

import com.management.employee.system.controller.response.ScopeUpdateResponse;
import com.management.employee.system.model.Scopes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScopesMapper {

    ScopeUpdateResponse toResponse(Scopes scopes);
}
