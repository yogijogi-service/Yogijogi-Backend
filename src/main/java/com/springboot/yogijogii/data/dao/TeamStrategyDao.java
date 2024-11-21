package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamStrategy.MatchStrategyDto;
import com.springboot.yogijogii.data.entity.TeamStrategy;

public interface TeamStrategyDao {

    TeamStrategy saveMatchStrategy (TeamStrategy teamStrategy);
}
