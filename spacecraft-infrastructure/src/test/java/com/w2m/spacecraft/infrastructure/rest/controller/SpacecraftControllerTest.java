package com.w2m.spacecraft.infrastructure.rest.controller;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.domain.usecase.SpacecraftUseCase;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftPageableRSDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRQDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRSDTO;
import com.w2m.spacecraft.infrastructure.rest.mapper.SpacecraftRestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpacecraftControllerTest {

  @InjectMocks
  private SpacecraftController spacecraftController;

  @Mock
  private SpacecraftRestMapper restMapper;

  @Mock
  private SpacecraftUseCase spacecraftUseCase;


  @Test
  @DisplayName("Should return response entity ok with filled response when create")
  void shouldReturnResponseEntityOkWithFilledResponseWhenCreateSpacecraft() {

    // given
    var spacecraftRQDTO = mock(SpacecraftRQDTO.class);
    var spacecraftIn = mock(Spacecraft.class);
    var spacecraftOut = mock(Spacecraft.class);
    var spacecraftRSDTO = mock(SpacecraftRSDTO.class);

    final var inOrder = inOrder(restMapper, spacecraftUseCase);
    when(restMapper.toSpacecraft(spacecraftRQDTO)).thenReturn(spacecraftIn);
    when(spacecraftUseCase.create(spacecraftIn)).thenReturn(spacecraftOut);
    when(restMapper.toSpacecraftRSDTO(spacecraftOut)).thenReturn(spacecraftRSDTO);

    // when
    var spacecraftRSDTOResponseEntity = spacecraftController.createSpacecraft(spacecraftRQDTO);

    // then
    assertThat(spacecraftRSDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(spacecraftRSDTOResponseEntity.getBody()).isEqualTo(spacecraftRSDTO);

    inOrder.verify(restMapper).toSpacecraft(spacecraftRQDTO);
    inOrder.verify(spacecraftUseCase).create(spacecraftIn);
    inOrder.verify(restMapper).toSpacecraftRSDTO(spacecraftOut);
    inOrder.verifyNoMoreInteractions();

  }

  @Test
  @DisplayName("Should return response entity ok with filled response when find by id")
  void shouldReturnResponseEntityOkWithFilledResponseWhenFindSpacecraftById() {

    // given
    var id = 1L;
    var spacecraftOut = mock(Spacecraft.class);
    var spacecraftRSDTO = mock(SpacecraftRSDTO.class);

    final var inOrder = inOrder(restMapper, spacecraftUseCase);
    when(spacecraftUseCase.findById(id)).thenReturn(spacecraftOut);
    when(restMapper.toSpacecraftRSDTO(spacecraftOut)).thenReturn(spacecraftRSDTO);

    // when
    var spacecraftRSDTOResponseEntity = spacecraftController.findSpacecraftById(id);

    // then
    assertThat(spacecraftRSDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(spacecraftRSDTOResponseEntity.getBody()).isEqualTo(spacecraftRSDTO);

    inOrder.verify(spacecraftUseCase).findById(id);
    inOrder.verify(restMapper).toSpacecraftRSDTO(spacecraftOut);
    inOrder.verifyNoMoreInteractions();

  }

  @Test
  @DisplayName("Should return response entity ok with filled response when update")
  void shouldReturnResponseEntityOkWithFilledResponseWhenUpdate() {

    // given
    var id = 1L;
    var spacecraftRQDTO = mock(SpacecraftRQDTO.class);
    var spacecraftIn = mock(Spacecraft.class);
    var spacecraftOut = mock(Spacecraft.class);
    var spacecraftRSDTO = mock(SpacecraftRSDTO.class);

    final var inOrder = inOrder(restMapper, spacecraftUseCase);
    when(restMapper.toSpacecraft(spacecraftRQDTO)).thenReturn(spacecraftIn);
    when(spacecraftUseCase.update(spacecraftIn)).thenReturn(spacecraftOut);
    when(restMapper.toSpacecraftRSDTO(spacecraftOut)).thenReturn(spacecraftRSDTO);

    // when
    var spacecraftRSDTOResponseEntity = spacecraftController.updateSpacecraft(id, spacecraftRQDTO);

    // then
    assertThat(spacecraftRSDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(spacecraftRSDTOResponseEntity.getBody()).isEqualTo(spacecraftRSDTO);

    inOrder.verify(restMapper).toSpacecraft(spacecraftRQDTO);
    inOrder.verify(spacecraftUseCase).update(spacecraftIn);
    inOrder.verify(restMapper).toSpacecraftRSDTO(spacecraftOut);
    inOrder.verifyNoMoreInteractions();

  }

  @Test
  @DisplayName("Should return response entity ok response when delete")
  void shouldReturnResponseEntityOkWhenDelete() {

    // given
    var id = 1L;

    // when
    spacecraftController.deleteSpacecraft(id);

    // then
    verify(spacecraftUseCase).delete(id);
    verifyNoMoreInteractions(spacecraftUseCase);
  }

  @Test
  @DisplayName("Should return response entity ok with filled response when find by name")
  void shouldReturnResponseEntityOkWithFilledResponseWhenFindByName() {

    // given
    var name = "testName";
    var page = 0;
    var size = 2;
    var spacecraftPageable = mock(SpacecraftPageable.class);
    var spacecraftPageableRSDTO = mock(SpacecraftPageableRSDTO.class);

    var inOrder = inOrder(spacecraftUseCase, restMapper);
    when(spacecraftUseCase.findAllByName(name, page, size)).thenReturn(spacecraftPageable);
    when(restMapper.toSpacecraftPageableRSDTO(spacecraftPageable)).thenReturn(spacecraftPageableRSDTO);

    // when
    var spacecraftPageableRSDTOResponseEntity = spacecraftController.findSpacecrafts(name, page, size);

    // then
    assertThat(spacecraftPageableRSDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(spacecraftPageableRSDTOResponseEntity.getBody()).isEqualTo(spacecraftPageableRSDTO);

    inOrder.verify(spacecraftUseCase).findAllByName(name, page, size);
    inOrder.verify(restMapper).toSpacecraftPageableRSDTO(spacecraftPageable);

  }

  @Test
  @DisplayName("Should return response entity ok with filled response when find without name")
  void shouldReturnResponseEntityOkWithFilledResponseWhenFindWithoutName() {

    // given
    var page = 0;
    var size = 2;
    var spacecraftPageable = mock(SpacecraftPageable.class);
    var spacecraftPageableRSDTO = mock(SpacecraftPageableRSDTO.class);

    var inOrder = inOrder(spacecraftUseCase, restMapper);
    when(spacecraftUseCase.findAll(page, size)).thenReturn(spacecraftPageable);
    when(restMapper.toSpacecraftPageableRSDTO(spacecraftPageable)).thenReturn(spacecraftPageableRSDTO);

    // when
    var spacecraftPageableRSDTOResponseEntity = spacecraftController.findSpacecrafts(null, page, size);

    // then
    assertThat(spacecraftPageableRSDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(spacecraftPageableRSDTOResponseEntity.getBody()).isEqualTo(spacecraftPageableRSDTO);

    inOrder.verify(spacecraftUseCase).findAll(page, size);
    inOrder.verify(restMapper).toSpacecraftPageableRSDTO(spacecraftPageable);

  }


}
