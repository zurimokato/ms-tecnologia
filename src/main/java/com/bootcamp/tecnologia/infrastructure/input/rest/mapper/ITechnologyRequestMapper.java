package com.bootcamp.tecnologia.infrastructure.input.rest.mapper;

import com.bootcamp.tecnologia.domain.model.Technology;
import com.bootcamp.tecnologia.infrastructure.input.rest.dto.TechnologyRequest;
import com.bootcamp.tecnologia.infrastructure.input.rest.dto.TechnologyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITechnologyRequestMapper {

    @Mapping(target = "id", ignore = true)
    Technology toDomain(TechnologyRequest request);

    TechnologyResponse toResponse(Technology technology);
}
