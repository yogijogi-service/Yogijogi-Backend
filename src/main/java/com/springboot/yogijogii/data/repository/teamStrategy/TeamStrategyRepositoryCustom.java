package com.springboot.yogijogii.data.repository.teamStrategy;

import com.springboot.yogijogii.data.entity.TeamStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamStrategyRepositoryCustom {
    Optional<List<TeamStrategy>> findTeamStrategyByDate(Long teamId, LocalDate date);
    Optional<List<TeamStrategy>> findAllTeamStrategyByDateRange(Long teamId, LocalDate startOfMonth, LocalDate endOfMonth);
}
