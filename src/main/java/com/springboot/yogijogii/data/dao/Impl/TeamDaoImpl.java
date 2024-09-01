package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeamDaoImpl implements TeamDao {

    private final TeamRepository teamRepository;

    @Override
    public void save(Team team) {
        teamRepository.save(team);
    }
}
