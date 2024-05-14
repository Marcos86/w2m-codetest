package com.w2m.spacecraft.domain.repository;


import com.w2m.spacecraft.domain.model.Spacecraft;
import com.w2m.spacecraft.domain.model.SpacecraftPageable;

public interface SpacecraftRepository {


    Spacecraft findById(long id);

    SpacecraftPageable findAll(int page, int size);

    SpacecraftPageable findAllByName(String name, int page, int size);

    Spacecraft create(Spacecraft spacecraft);

    Spacecraft update(Spacecraft spacecraft);

    void delete(long id);
}
