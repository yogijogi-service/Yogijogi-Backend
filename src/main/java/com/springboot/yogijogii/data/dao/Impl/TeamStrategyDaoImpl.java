package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.TeamStrategyDao;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamStrategyMonthlyDto;
import com.springboot.yogijogii.data.entity.TeamStrategy;
import com.springboot.yogijogii.data.repository.teamStrategy.TeamStrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TeamStrategyDaoImpl implements TeamStrategyDao {

    private final TeamStrategyRepository teamStrategyRepository;


    @Override
    public TeamStrategy saveMatchStrategy(TeamStrategy teamStrategy) {

        return teamStrategyRepository.save(teamStrategy);
    }

    @Override
    public List<TeamStrategyMonthlyDto> findTeamStrategyMonthlyInfo(Long teamId,LocalDate date) {
        Optional<List<TeamStrategy>> teamStrategyList = teamStrategyRepository.findTeamStrategyByDate(teamId, date);
        List<TeamStrategyMonthlyDto> teamStrategyMonthlyDtoList = new ArrayList<>();
        for(TeamStrategy teamStrategy : teamStrategyList.get()){
            TeamStrategyMonthlyDto teamStrategyMonthlyDto = new TeamStrategyMonthlyDto(
                    teamStrategy.getId(),
                    teamStrategy.getTeam().getTeamName(),
                    teamStrategy.getOpposingTeam(),
                    teamStrategy.getMatchStartTime(),
                    teamStrategy.getMatchEndTime()
            );
            teamStrategyMonthlyDtoList.add(teamStrategyMonthlyDto);
        }
        return teamStrategyMonthlyDtoList;
    }
    @Override
    public List<TeamStrategyMonthlyDto> findAllTeamStrategyMonthlyInfo(Long teamId,String date) {
        LocalDate startOfMonth = LocalDate.parse(date + "-01");
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Optional<List<TeamStrategy>> teamStrategyList = teamStrategyRepository.findAllTeamStrategyByDateRange(teamId, startOfMonth,endOfMonth);
        List<TeamStrategyMonthlyDto> teamStrategyMonthlyDtoList = new ArrayList<>();
        for(TeamStrategy teamStrategy : teamStrategyList.get()){
            TeamStrategyMonthlyDto teamStrategyMonthlyDto = new TeamStrategyMonthlyDto(
                    teamStrategy.getId(),
                    teamStrategy.getTeam().getTeamName(),
                    teamStrategy.getOpposingTeam(),
                    teamStrategy.getMatchStartTime(),
                    teamStrategy.getMatchEndTime()
            );
            teamStrategyMonthlyDtoList.add(teamStrategyMonthlyDto);
        }
        return teamStrategyMonthlyDtoList;
    }
}
