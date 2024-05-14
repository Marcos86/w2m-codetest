package com.w2m.spacecraft.domain.usecase;


import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;

public interface SpacecraftUseCase {


    Spacecraft findById(long id);

    SpacecraftPageable findAllByName(String name, int page, int size);

    SpacecraftPageable findAll(int page, int size);

    Spacecraft create(Spacecraft spacecraft);

    Spacecraft update(Spacecraft spacecraft);

    void delete(long id);
}
