package com.w2m.spacecraft.infrastructure.persistence.adapter;

import com.w2m.spacecraft.domain.exception.DataNotFoundException;
import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.infrastructure.persistence.entity.SpacecraftEntity;
import com.w2m.spacecraft.infrastructure.persistence.mapper.SpacecraftEntityMapper;
import com.w2m.spacecraft.infrastructure.persistence.repository.SpacecraftJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.createList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpacecraftRepositoryAdapterTest {

  @InjectMocks
  private SpacecraftRepositoryAdapter spacecraftRepositoryAdapter;

  @Mock
  private SpacecraftEntityMapper spacecraftEntityMapper;

  @Mock
  private SpacecraftJpaRepository spacecraftJpaRepository;

  @Test
  @DisplayName("should find by id when exists")
  void shouldFindByIdAndReturnSpacecraftWhenExists() {

    // given
    var id = 1L;
    var spacecraftEntity = mock(SpacecraftEntity.class);
    var spacecraft = mock(Spacecraft.class);

    final var inOrder = inOrder(spacecraftJpaRepository, spacecraftEntityMapper);
    when(spacecraftJpaRepository.findById(id)).thenReturn(Optional.of(spacecraftEntity));
    when(spacecraftEntityMapper.toSpacecraft(spacecraftEntity)).thenReturn(spacecraft);

    // when
    var spacecraftResult = spacecraftRepositoryAdapter.findById(id);

    // then
    assertThat(spacecraftResult).isEqualTo(spacecraft);

    inOrder.verify(spacecraftJpaRepository).findById(id);
    inOrder.verify(spacecraftEntityMapper).toSpacecraft(spacecraftEntity);
    inOrder.verifyNoMoreInteractions();

  }


  @Test
  @DisplayName("should not find by id and return DataNotFoundException when not exists")
  void shouldNotFindByIdAndReturnDataNotFoundExceptionWhenNotExists() {

    // given
    var id = 1L;
    var errorCodeExpected = "1";
    var messageExpected = String.format("Spacecraft selected with id: %s not found", id);

    // when
    var dataNotFoundException = assertThrows(DataNotFoundException.class, () -> spacecraftRepositoryAdapter.findById(id));

    // then
    assertThat(dataNotFoundException.getCode()).isEqualTo(errorCodeExpected);
    assertThat(dataNotFoundException.getMessage()).isEqualTo(messageExpected);

    verify(spacecraftJpaRepository).findById(id);
    verifyNoMoreInteractions(spacecraftJpaRepository);

  }


  @Test
  @DisplayName("should create and return spacecraft")
  void shouldCreateAndReturnSpacecraft() {

    // given
    var spacecraft = mock(Spacecraft.class);
    var spacecraftEntity = mock(SpacecraftEntity.class);
    var spacecraftEntityCreated = mock(SpacecraftEntity.class);
    var spacecraftCreated = mock(Spacecraft.class);

    var inOrder = inOrder(spacecraftEntityMapper, spacecraftJpaRepository);
    when(spacecraftEntityMapper.toSpacecraftEntity(spacecraft)).thenReturn(spacecraftEntity);
    when(spacecraftJpaRepository.save(spacecraftEntity)).thenReturn(spacecraftEntityCreated);
    when(spacecraftEntityMapper.toSpacecraft(spacecraftEntityCreated)).thenReturn(spacecraftCreated);

    // when
    var spacecraftResult = spacecraftRepositoryAdapter.create(spacecraft);

    // then
    assertThat(spacecraftResult).isEqualTo(spacecraftCreated);

    inOrder.verify(spacecraftEntityMapper).toSpacecraftEntity(spacecraft);
    inOrder.verify(spacecraftJpaRepository).save(spacecraftEntity);
    inOrder.verify(spacecraftEntityMapper).toSpacecraft(spacecraftEntityCreated);

    inOrder.verifyNoMoreInteractions();

  }

  @Test
  @DisplayName("should update and return spacecraft when exists")
  void shouldUpdateReturnSpacecraftAndReturnSpacecraftWhenExists() {

    // given
    var id = 1L;
    var name1 = "name1";
    var serie1 = "serie1";
    var film1 = "film1";
    var spacecraftEntity = createSpacecraftEntity(id, name1, serie1, film1);
    var name2 = "name2";
    var serie2 = "serie2";
    var film2 = "film2";
    var spacecraftUpdate = createSpacecraft(id, name2, serie2, film2);
    var inOrder = inOrder(spacecraftJpaRepository, spacecraftEntityMapper);
    when(spacecraftJpaRepository.findById(id)).thenReturn(Optional.of(spacecraftEntity));
    when(spacecraftJpaRepository.save(spacecraftEntity)).thenReturn(spacecraftEntity);
    when(spacecraftEntityMapper.toSpacecraft(spacecraftEntity)).thenReturn(spacecraftUpdate);

    // when
    var spacecraftResult = spacecraftRepositoryAdapter.update(spacecraftUpdate);

    // then
    assertThat(spacecraftResult).isEqualTo(spacecraftUpdate);

    inOrder.verify(spacecraftJpaRepository).findById(id);
    inOrder.verify(spacecraftJpaRepository).save(spacecraftEntity);
    inOrder.verify(spacecraftEntityMapper).toSpacecraft(spacecraftEntity);

    inOrder.verifyNoMoreInteractions();

  }


  @Test
  @DisplayName("should not update and throw DataNotFoundException when not exists")
  void shouldNotUpdateAndThrowDataNotFoundExceptionWhenNotExists() {

    // given
    var id = 1L;
    var name2 = "nameUpdate";
    var serie2 = "serie2";
    var film2 = "film2";
    var spacecraftUpdate = createSpacecraft(id, name2, serie2, film2);
    var errorCodeExpected = "1";
    var messageExpected = String.format("Spacecraft selected with id: %s not found", id);

    when(spacecraftJpaRepository.findById(id)).thenReturn(Optional.empty());

    // when
    var dataNotFoundException = assertThrows(DataNotFoundException.class, () -> spacecraftRepositoryAdapter.update(spacecraftUpdate));

    // then
    assertThat(dataNotFoundException.getCode()).isEqualTo(errorCodeExpected);
    assertThat(dataNotFoundException.getMessage()).isEqualTo(messageExpected);

    verifyNoMoreInteractions(spacecraftJpaRepository, spacecraftEntityMapper);

  }


  @Test
  @DisplayName("should delete properly when exists")
  void shouldDeleteProperlyWhenExists() {

    // given
    var id = 1L;
    var spacecraftEntity = mock(SpacecraftEntity.class);

    when(spacecraftJpaRepository.findById(id)).thenReturn(Optional.of(spacecraftEntity));

    // when
    spacecraftRepositoryAdapter.delete(id);

    // then
    verify(spacecraftJpaRepository).deleteById(id);
    verifyNoMoreInteractions(spacecraftJpaRepository);

  }

  @Test
  @DisplayName("should not delete and throw DataNotFoundException when not exists")
  void shouldNotDeleteAndThrowDataNotFoundExceptionWhenNotExists() {

    var id = 1L;
    var errorCodeExpected = "1";
    var messageExpected = String.format("Spacecraft selected with id: %s not found", id);

    when(spacecraftJpaRepository.findById(id)).thenReturn(Optional.empty());

    // when
    var dataNotFoundException = assertThrows(DataNotFoundException.class, () -> spacecraftRepositoryAdapter.delete(id));

    // then
    assertThat(dataNotFoundException.getCode()).isEqualTo(errorCodeExpected);
    assertThat(dataNotFoundException.getMessage()).isEqualTo(messageExpected);

    verifyNoMoreInteractions(spacecraftJpaRepository, spacecraftEntityMapper);

  }


  @Test
  @DisplayName("should find all by name and return spacecraft pageable")
  void shouldFindAllByNameAndReturnSpacecraftPageable() {

    // given
    var name = "testName";
    var page = 0;
    var size = 10;
    var spacecraftEntities = createList(SpacecraftEntity.class);
    var spacecraftPageable = mock(SpacecraftPageable.class);
    Page<SpacecraftEntity> pagedSpacecraftEntities = new PageImpl(spacecraftEntities);

    var inOrder = inOrder(spacecraftJpaRepository, spacecraftEntityMapper);
    when(spacecraftJpaRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(pagedSpacecraftEntities);
    when(spacecraftEntityMapper.toSpacecraftPageable(anyLong(), anyList(), anyLong(), anyInt())).thenReturn(spacecraftPageable);

    // when
    var spacecraftPageableResult = spacecraftRepositoryAdapter.findAllByName(name, page, size);

    // then
    assertThat(spacecraftPageableResult).isEqualTo(spacecraftPageable);

    inOrder.verify(spacecraftJpaRepository).findAll(any(Example.class), any(Pageable.class));
    inOrder.verify(spacecraftEntityMapper).toSpacecraftPageable(anyLong(), anyList(), anyLong(), anyInt());
    inOrder.verifyNoMoreInteractions();

  }

  @Test
  @DisplayName("should find all return spacecraft pageable")
  void shouldFindAllReturnSpacecraftPageable() {

    // given
    var page = 0;
    var size = 10;
    var spacecraftEntities = createList(SpacecraftEntity.class);
    var spacecraftPageable = mock(SpacecraftPageable.class);
    Page<SpacecraftEntity> pagedSpacecraftEntities = new PageImpl(spacecraftEntities);

    var inOrder = inOrder(spacecraftJpaRepository, spacecraftEntityMapper);
    when(spacecraftJpaRepository.findAll(any(Pageable.class))).thenReturn(pagedSpacecraftEntities);
    when(spacecraftEntityMapper.toSpacecraftPageable(anyLong(), anyList(), anyLong(), anyInt())).thenReturn(spacecraftPageable);

    // when
    var spacecraftPageableResult = spacecraftRepositoryAdapter.findAll(page, size);

    // then
    assertThat(spacecraftPageableResult).isEqualTo(spacecraftPageable);

    inOrder.verify(spacecraftJpaRepository).findAll(any(Pageable.class));
    inOrder.verify(spacecraftEntityMapper).toSpacecraftPageable(anyLong(), anyList(), anyLong(), anyInt());
    inOrder.verifyNoMoreInteractions();

  }



  private SpacecraftEntity createSpacecraftEntity(final long id, final String name, final String series, final String films) {
    var spacecraftEntity = new SpacecraftEntity();
    spacecraftEntity.setId(id);
    spacecraftEntity.setName(name);
    spacecraftEntity.setSeries(series);
    spacecraftEntity.setFilms(films);
    return spacecraftEntity;
  }

  private Spacecraft createSpacecraft(final long id, final String name, final String series, final String films) {
    var spacecraft = new Spacecraft();
    spacecraft.setId(id);
    spacecraft.setName(name);
    spacecraft.setSeries(series);
    spacecraft.setFilms(films);
    return spacecraft;
  }

}