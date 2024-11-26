package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.data.entity.Team;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TeamMemberDao {
    void saveTeamMember(TeamMember teamMember);

    void saveServiceRole(ServiceRole serviceRole);
    boolean existsByMemberAndTeamAndRole(Member member, Team team, String role);

    boolean existsByMemberAndTeam(Member member, Team team);

    List<TeamMember> findByMember(Member member);

    List<TeamMember> findByTeam(Team team, Sort sortOrder);

    List<TeamMember> findByTeamAndPosition(Team team, String position, Sort sortOrder);

    TeamMember findById(Long teamMemberId);

    void save(TeamMember teamMember);

    TeamMember findByMemberAndTeam(Member member, Team team);

    void delete(TeamMember teamMember);

    List<TeamMemberByPositionDto> getTeamMemberByUserIdAndPosition(Long userId, String position);
    boolean isTeamMemberAndManager(Long userId, Long teamId);

}
