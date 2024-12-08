package com.springboot.yogijogii.data.repository.teamMember;

import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.Team;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>,TeamMemberRepositoryCustom {
    boolean existsByMemberAndTeamAndRole(Member member, Team team, String role);

    boolean existsByMemberAndTeam(Member member, Team team);

    List<TeamMember> findByMember(Member member);

    List<TeamMember> findByTeam(Team team, Sort sortOrder);

    List<TeamMember> findByTeamAndPosition(Team team, String position, Sort sortOrder);

    TeamMember findTeamMemberById(Long teamMemberId);

    TeamMember findTeamMemberByMemberAndTeam(Member member, Team team);
}
