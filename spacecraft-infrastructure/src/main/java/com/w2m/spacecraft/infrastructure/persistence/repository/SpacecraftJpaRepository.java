package com.w2m.spacecraft.infrastructure.persistence.repository;

import com.w2m.spacecraft.infrastructure.persistence.entity.SpacecraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpacecraftJpaRepository extends JpaRepository<SpacecraftEntity, Long> {}
