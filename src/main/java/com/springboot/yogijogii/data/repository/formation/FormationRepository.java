package com.springboot.yogijogii.data.repository.formation;

import com.springboot.yogijogii.data.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormationRepository extends JpaRepository<Formation, Long>,FormationRepositoryCustom{

}
