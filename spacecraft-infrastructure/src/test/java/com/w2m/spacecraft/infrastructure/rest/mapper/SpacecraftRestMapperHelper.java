package com.w2m.spacecraft.infrastructure.rest.mapper;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRQDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRSDTO;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpacecraftRestMapperHelper {

  SpacecraftRQDTO toSpacecraftRQDTO(Spacecraft spacecraft);

  Spacecraft toSpacecraft(SpacecraftRSDTO spacecraftRSDTO);

}
