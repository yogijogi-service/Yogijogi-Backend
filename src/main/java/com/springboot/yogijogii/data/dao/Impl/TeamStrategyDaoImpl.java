package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.TeamStrategyDao;
import com.springboot.yogijogii.data.entity.TeamStrategy;
import com.springboot.yogijogii.data.repository.teamStrategy.TeamStrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class TeamStrategyDaoImpl implements TeamStrategyDao {

    private final TeamStrategyRepository teamStrategyRepository;


    @Override
    public TeamStrategy saveMatchStrategy(TeamStrategy teamStrategy) {

        return teamStrategyRepository.save(teamStrategy);
    }

}
