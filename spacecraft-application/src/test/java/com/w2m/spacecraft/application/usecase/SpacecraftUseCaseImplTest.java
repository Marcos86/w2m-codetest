package com.w2m.spacecraft.application.usecase;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.domain.repository.SpacecraftRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpacecraftUseCaseImplTest {

  @InjectMocks
  private SpacecraftUseCaseImpl spacecraftUseCase;

  @Mock
  private SpacecraftRepository spacecraftRepository;


  @Test
  @DisplayName("Should return spacecraft when find by id")
  void shouldReturnSpacecraftWhenFindById() {

    // given
    var id = 1L;
    var spacecraft = mock(Spacecraft.class);
    when(spacecraftRepository.findById(id)).thenReturn(spacecraft);

    // when
    var spacecraftResult = spacecraftUseCase.findById(id);

    // then
    assertThat(spacecraftResult).isEqualTo(spacecraft);
    verify(spacecraftRepository).findById(id);
    verifyNoMoreInteractions(spacecraftRepository);

  }

  @Test
  @DisplayName("Should return spacecraft pageable when find all by name")
  void shouldReturnSpacecraftWhenFindAllByName() {

    // given
    var name = "testName";
    var page = 0;
    var size = 2;
    var spacecraftPageable = mock(SpacecraftPageable.class);
    when(spacecraftRepository.findAllByName(name, page, size)).thenReturn(spacecraftPageable);

    // when
    var spacecraftPageableResult = spacecraftUseCase.findAllByName(name, page, size);

    // then
    assertThat(spacecraftPageableResult).isEqualTo(spacecraftPageable);
    verify(spacecraftRepository).findAllByName(name, page, size);
    verifyNoMoreInteractions(spacecraftRepository);

  }

  @Test
  @DisplayName("Should return spacecraft pageable when find all")
  void shouldReturnSpacecraftWhenFindAll() {

    // given
    var page = 0;
    var size = 2;
    var spacecraftPageable = mock(SpacecraftPageable.class);
    when(spacecraftRepository.findAll(page, size)).thenReturn(spacecraftPageable);

    // when
    var spacecraftPageableResult = spacecraftUseCase.findAll(page, size);

    // then
    assertThat(spacecraftPageableResult).isEqualTo(spacecraftPageable);
    verify(spacecraftRepository).findAll(page, size);
    verifyNoMoreInteractions(spacecraftRepository);

  }

  @Test
  @DisplayName("Should return spacecraft when create")
  void shouldReturnSpacecraftWhenCreate() {

    // given
    var spacecraftIn = mock(Spacecraft.class);
    var spacecraftOut = mock(Spacecraft.class);
    when(spacecraftRepository.create(spacecraftIn)).thenReturn(spacecraftOut);

    // when
    var spacecraftResult = spacecraftUseCase.create(spacecraftIn);

    // then
    verify(spacecraftRepository).create(spacecraftIn);

    assertThat(spacecraftResult).isEqualTo(spacecraftOut);

  }

  @Test
  @DisplayName("Should return spacecraft when update")
  void shouldReturnSpacecraftWhenUpdate() {

    // given
    var spacecraftIn = mock(Spacecraft.class);
    var spacecraftOut = mock(Spacecraft.class);
    when(spacecraftRepository.update(spacecraftIn)).thenReturn(spacecraftOut);

    // when
    var spacecraftResult = spacecraftUseCase.update(spacecraftIn);

    // then
    verify(spacecraftRepository).update(spacecraftIn);

    assertThat(spacecraftResult).isEqualTo(spacecraftOut);

  }

  @Test
  @DisplayName("Should delete properly when delete by id")
  void shouldDeleteProperlyWhenDeleteById() {

    // given
    var id = 1L;

    // when
    spacecraftUseCase.delete(id);

    // then
    verify(spacecraftRepository).delete(id);
    verifyNoMoreInteractions(spacecraftRepository);

  }

}
