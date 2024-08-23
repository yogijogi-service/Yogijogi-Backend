package com.springboot.yogijogii.data.repository.team;


import com.springboot.yogijogii.data.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long>,TeamRepositoryCustom {

    Team findByInviteCode(String inviteCode);
    Team findByTeamId(Long teamId);
    Team findTeamWithMemberByTeamId(Long id);





}
