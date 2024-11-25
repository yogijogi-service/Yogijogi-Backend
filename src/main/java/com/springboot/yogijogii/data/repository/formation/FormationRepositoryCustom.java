package com.springboot.yogijogii.data.repository.formation;

import com.springboot.yogijogii.data.entity.Formation;

import java.util.List;
import java.util.Optional;

public interface FormationRepositoryCustom {
    Optional<Formation> findByName(Long teamId,String name);
}
