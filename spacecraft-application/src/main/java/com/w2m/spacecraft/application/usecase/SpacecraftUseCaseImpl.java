package com.w2m.spacecraft.application.usecase;

import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.domain.repository.SpacecraftRepository;
import com.w2m.spacecraft.domain.usecase.SpacecraftUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpacecraftUseCaseImpl implements SpacecraftUseCase {

    private final SpacecraftRepository spacecraftRepository;

    @Override
    public Spacecraft findById(final long id) {
      return spacecraftRepository.findById(id);
    }

    @Override
    public SpacecraftPageable findAllByName(final String name, final int page, final int size) {
        return spacecraftRepository.findAllByName(name, page, size);
    }

    @Override
    public SpacecraftPageable findAll(final int page, final int size) {
        return spacecraftRepository.findAll(page, size);
    }

    @Override
    public Spacecraft create(final Spacecraft spacecraft) {
        return spacecraftRepository.create(spacecraft);
    }

    @Override
    public Spacecraft update(final Spacecraft spacecraft) {
        return spacecraftRepository.update(spacecraft);
    }

    @Override
    public void delete(final long id) {
        spacecraftRepository.delete(id);
    }
}
