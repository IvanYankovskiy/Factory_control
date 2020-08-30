package com.factory.control.controller.mapper;

import com.factory.control.controller.dto.ExtruderDTO;
import com.factory.control.domain.entities.device.Extruder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = DeviceMapper.class, mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExtruderMapper {

    @Mapping(target = "circumference", source = "dto.circumference")
    Extruder fromDtoToEntity(ExtruderDTO dto);

    @Mapping(target = "circumference", source = "entity.circumference")
    ExtruderDTO fromEntityToDto(Extruder entity);

    default List<ExtruderDTO> fromEntitiesToDTOs(List<Extruder> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities
                .stream()
                .map(this::fromEntityToDto)
                .collect(Collectors.toList());
    }
}
