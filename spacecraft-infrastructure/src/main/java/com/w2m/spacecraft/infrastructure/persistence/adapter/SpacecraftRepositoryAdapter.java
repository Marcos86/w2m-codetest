package com.w2m.spacecraft.infrastructure.persistence.adapter;

import com.w2m.spacecraft.domain.exception.DataNotFoundException;
import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.domain.repository.SpacecraftRepository;
import com.w2m.spacecraft.infrastructure.persistence.entity.SpacecraftEntity;
import com.w2m.spacecraft.infrastructure.persistence.mapper.SpacecraftEntityMapper;
import com.w2m.spacecraft.infrastructure.persistence.repository.SpacecraftJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.w2m.spacecraft.infrastructure.config.CachingConfig.KEY_ID;
import static com.w2m.spacecraft.infrastructure.config.CachingConfig.KEY_NAME_PAGE_SIZE;
import static com.w2m.spacecraft.infrastructure.config.CachingConfig.KEY_PAGE_SIZE;
import static com.w2m.spacecraft.infrastructure.config.CachingConfig.NAME_SPACECRAFTS_CACHE;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@RequiredArgsConstructor
public class SpacecraftRepositoryAdapter implements SpacecraftRepository {

    private static final String NOT_FOUND_ERROR_CODE = "1";

    private static final String NOT_FOUND_ERROR_MESSAGE = "Spacecraft selected with id: %s not found";

    private static final String FIELD_ID = "id";

    private static final String FIELD_NAME = "name";

    private final SpacecraftEntityMapper spacecraftEntityMapper;

    private final SpacecraftJpaRepository spacecraftJpaRepository;

    @Cacheable(value = NAME_SPACECRAFTS_CACHE, key = KEY_ID)
    @Override
    public Spacecraft findById(final long id) {
        var spacecraftEntity = findSpaceCraft(id);
        return spacecraftEntityMapper.toSpacecraft(spacecraftEntity);
    }

    @CacheEvict(value = NAME_SPACECRAFTS_CACHE, allEntries = true)
    @Override
    public Spacecraft create(final Spacecraft spacecraft) {
        var spacecraftEntity = spacecraftEntityMapper.toSpacecraftEntity(spacecraft);
        var spacecraftEntitySaved = spacecraftJpaRepository.save(spacecraftEntity);
        return spacecraftEntityMapper.toSpacecraft(spacecraftEntitySaved);
    }

    @CacheEvict(value = NAME_SPACECRAFTS_CACHE, allEntries = true)
    @Override
    public Spacecraft update(Spacecraft spacecraft) {
        var spacecraftEntityFound = findSpaceCraft(spacecraft.getId());
        updateFields(spacecraft, spacecraftEntityFound);
        var spacecraftEntity = spacecraftJpaRepository.save(spacecraftEntityFound);
        return spacecraftEntityMapper.toSpacecraft(spacecraftEntity);
    }


    @CacheEvict(value = NAME_SPACECRAFTS_CACHE, allEntries = true)
    @Override
    public void delete(final long id) {
        findSpaceCraft(id);
        spacecraftJpaRepository.deleteById(id);
    }

    @Cacheable(value = NAME_SPACECRAFTS_CACHE, key = KEY_NAME_PAGE_SIZE)
    @Override
    public SpacecraftPageable findAllByName(final String name, final int page, final int size) {

        var pageable = PageRequest.of(page, size, Sort.by(FIELD_ID).ascending());
        var spacecraftEntity = new SpacecraftEntity();
        spacecraftEntity.setName(name);
        var matcher = ExampleMatcher.matching().withMatcher(FIELD_NAME, contains());
        var example = Example.of(spacecraftEntity, matcher);
        var pageResult = spacecraftJpaRepository.findAll(example, pageable);

        return spacecraftEntityMapper.toSpacecraftPageable(pageResult.getTotalElements(),
                getSpacecraftList(pageResult),
                pageResult.getTotalPages(),
                pageResult.getNumber());

    }

    @Cacheable(value = NAME_SPACECRAFTS_CACHE, key = KEY_PAGE_SIZE)
    @Override
    public SpacecraftPageable findAll(final int page, final int size) {

        var pageable = PageRequest.of(page, size, Sort.by(FIELD_ID).ascending());
        var pageResult = spacecraftJpaRepository.findAll(pageable);

        return spacecraftEntityMapper.toSpacecraftPageable(pageResult.getTotalElements(),
                getSpacecraftList(pageResult),
                pageResult.getTotalPages(),
                pageResult.getNumber());

    }

    private SpacecraftEntity findSpaceCraft(final long id) {
        return spacecraftJpaRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(NOT_FOUND_ERROR_CODE,
                        String.format(NOT_FOUND_ERROR_MESSAGE, id)));
    }

    private void updateFields(Spacecraft spacecraft, SpacecraftEntity spacecraftEntity) {
        // We consider that if field is present we want to update it. If not present
        // Other behaviour: could be overwritten if not present (but we decide emulate partial/total update)
        var name = spacecraft.getName();
        if (null != name) {
            spacecraftEntity.setName(name);
        }
        var series = spacecraft.getSeries();
        if (null != series) {
            spacecraftEntity.setSeries(series);
        }
        var films = spacecraft.getFilms();
        if (null != films) {
            spacecraftEntity.setFilms(films);
        }

    }

    private List<Spacecraft> getSpacecraftList(Page<SpacecraftEntity> pageResult) {
        return pageResult.getContent()
                .stream()
                .map(spacecraftEntityMapper::toSpacecraft)
                .toList();
    }

}
