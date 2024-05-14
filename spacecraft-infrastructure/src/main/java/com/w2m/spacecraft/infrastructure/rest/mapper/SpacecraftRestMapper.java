package com.w2m.spacecraft.infrastructure.rest.mapper;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftPageableRSDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRQDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRSDTO;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpacecraftRestMapper {

  Spacecraft toSpacecraft(SpacecraftRQDTO spacecraftRQDTO);

  SpacecraftRSDTO toSpacecraftRSDTO(Spacecraft spacecraft);

  SpacecraftPageableRSDTO toSpacecraftPageableRSDTO(SpacecraftPageable spacecraftPageable);

}
