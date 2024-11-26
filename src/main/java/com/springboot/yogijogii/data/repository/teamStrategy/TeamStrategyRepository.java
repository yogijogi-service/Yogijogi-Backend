package com.springboot.yogijogii.data.repository.teamStrategy;

import com.springboot.yogijogii.data.entity.TeamStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamStrategyRepository extends JpaRepository<TeamStrategy,Long>,TeamStrategyRepositoryCustom {

}
