package com.w2m.spacecraft.infrastructure.persistence.mapper;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.infrastructure.persistence.entity.SpacecraftEntity;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpacecraftEntityMapper {

  Spacecraft toSpacecraft(SpacecraftEntity spacecraftEntity);

  SpacecraftEntity toSpacecraftEntity(Spacecraft spacecraft);

  SpacecraftPageable toSpacecraftPageable(long totalItems,List<Spacecraft> items, long totalPages, int currentPage);

}
