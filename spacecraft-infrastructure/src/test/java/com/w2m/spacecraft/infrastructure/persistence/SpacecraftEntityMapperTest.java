package com.w2m.spacecraft.infrastructure.persistence;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.infrastructure.persistence.entity.SpacecraftEntity;
import com.w2m.spacecraft.infrastructure.persistence.mapper.SpacecraftEntityMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.createList;

@ExtendWith(MockitoExtension.class)
class SpacecraftEntityMapperTest {

  @Spy
  private final SpacecraftEntityMapper spacecraftEntityMapper = Mappers.getMapper(SpacecraftEntityMapper.class);


  @Test
  @DisplayName("should map toSpacecraft and toEntity properly")
  void mapToSpacecraftAndToEntityProperly() {

    // given
    var spacecraftEntity = Instancio.create(SpacecraftEntity.class);

    // when
    var spacecraft = this.spacecraftEntityMapper.toSpacecraft(spacecraftEntity);
    var spacecraftEntityResult = this.spacecraftEntityMapper.toSpacecraftEntity(spacecraft);

    // then
    assertThat(spacecraftEntityResult).isEqualTo(spacecraftEntity);
  }

  @Test
  @DisplayName("should map toSpacecraftPageable properly")
  void mapToSpacecraftPageableProperly() {

    // given
    var totalItems = 10;
    var totalPages = 2;
    var currentPage = 1;
    var items = createList(Spacecraft.class);

    // when
    var spacecraftPageableResult = spacecraftEntityMapper.toSpacecraftPageable(totalItems, items, totalPages, currentPage);

    assertThat(spacecraftPageableResult.getTotalItems()).isEqualTo(totalItems);
    assertThat(spacecraftPageableResult.getItems()).isEqualTo(items);
    assertThat(spacecraftPageableResult.getTotalPages()).isEqualTo(totalPages);
    assertThat(spacecraftPageableResult.getCurrentPage()).isEqualTo(currentPage);

  }
}