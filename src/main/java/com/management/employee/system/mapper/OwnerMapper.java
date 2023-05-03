package com.management.employee.system.mapper;

import com.management.employee.system.controller.response.OwnerResponse;
import com.management.employee.system.model.Owner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    OwnerResponse toResponse(Owner owner);
}
