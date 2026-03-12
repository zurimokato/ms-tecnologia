package com.bootcamp.tecnologia.infrastructure.output.mysql.mapper;

import com.bootcamp.tecnologia.domain.model.Technology;
import com.bootcamp.tecnologia.infrastructure.output.mysql.entity.TechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITechnologyEntityMapper {

    TechnologyEntity toEntity(Technology technology);

    Technology toDomain(TechnologyEntity entity);
}
