package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamStrategy.MatchStrategyDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamStrategyMonthlyDto;
import com.springboot.yogijogii.data.entity.TeamStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamStrategyDao {

    TeamStrategy saveMatchStrategy (TeamStrategy teamStrategy);
    List<TeamStrategyMonthlyDto> findTeamStrategyMonthlyInfo(Long teamId, LocalDate date);
    List<TeamStrategyMonthlyDto> findAllTeamStrategyMonthlyInfo(Long teamId,String date);

}
