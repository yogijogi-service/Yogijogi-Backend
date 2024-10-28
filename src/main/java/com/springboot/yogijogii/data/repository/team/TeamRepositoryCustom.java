package com.springboot.yogijogii.data.repository.team;


import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterRequestDto;
import com.springboot.yogijogii.data.entity.Team;

import java.util.List;

public interface TeamRepositoryCustom {
    List<Team> searchTeams(SearchTeamFilterRequestDto searchTeamFilterRequestDto);

}
