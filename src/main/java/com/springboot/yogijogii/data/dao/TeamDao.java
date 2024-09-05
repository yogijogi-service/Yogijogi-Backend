package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.entity.Team;

public interface TeamDao {
    void save(Team team);

    Team findByTeamId(Long teamId);

    Team findByInviteCode(String inviteCode);
}
