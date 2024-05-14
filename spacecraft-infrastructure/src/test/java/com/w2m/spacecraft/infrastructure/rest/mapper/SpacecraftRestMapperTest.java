package com.w2m.spacecraft.infrastructure.rest.mapper;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRQDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.create;

@ExtendWith(MockitoExtension.class)
class SpacecraftRestMapperTest {

  @Spy
  private final SpacecraftRestMapper spacecraftRestMapper = Mappers.getMapper(SpacecraftRestMapper.class);

  @Spy
  private final SpacecraftRestMapperHelper spacecraftRestMapperHelper = Mappers.getMapper(SpacecraftRestMapperHelper.class);

  @Test
  @DisplayName("should map properly toSpacecraft")
  void mapProperlyToSpacecraft() {

    // given
    var spacecraftRQDTO = create(SpacecraftRQDTO.class);

    // when
    var spacecraft = this.spacecraftRestMapper.toSpacecraft(spacecraftRQDTO);

    // then
    var spacecraftRQDTOResult = this.spacecraftRestMapperHelper.toSpacecraftRQDTO(spacecraft);
    assertThat(spacecraftRQDTOResult).isEqualTo(spacecraftRQDTO);
  }


  @Test
  @DisplayName("should map properly toSpacecraftRSDTO")
  void mapProperlyToSpacecraftRSDTO() {

    // given
    var spacecraft = create(Spacecraft.class);

    // when
    var spacecraftRSDTO = this.spacecraftRestMapper.toSpacecraftRSDTO(spacecraft);

    // then
    var spacecraftResult = this.spacecraftRestMapperHelper.toSpacecraft(spacecraftRSDTO);
    assertThat(spacecraftResult).isEqualTo(spacecraft);
  }



}
