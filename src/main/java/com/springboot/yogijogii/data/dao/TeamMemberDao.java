package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.data.entity.Team;

import java.util.List;

public interface TeamMemberDao {
    void saveTeamMember(TeamMember teamMember);

    void saveServiceRole(ServiceRole serviceRole);
    boolean existsByMemberAndTeamAndRole(Member member, Team team, String role);

    boolean existsByMemberAndTeam(Member member, Team team);

    List<TeamMember> findByTeam(Team team);

    List<TeamMember> findByTeamAndPosition(Team team, String position);
}
