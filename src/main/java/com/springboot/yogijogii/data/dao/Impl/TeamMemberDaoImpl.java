package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.member.ServiceRoleRepository;
import com.springboot.yogijogii.data.repository.teamMember.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamMemberDaoImpl implements TeamMemberDao {
    private final TeamMemberRepository teamMemberRepository;
    private final ServiceRoleRepository serviceRoleRepository;

    @Override
    public void saveTeamMember(TeamMember teamMember) {
        teamMemberRepository.save(teamMember);
    }

    @Override
    public void saveServiceRole(ServiceRole serviceRole) {
        serviceRoleRepository.save(serviceRole);
    }

    @Override
    public boolean existsByMemberAndTeamAndRole(Member member, Team team, String role) {
        return teamMemberRepository.existsByMemberAndTeamAndRole(member, team, role);
    }
    @Override
    public boolean existsByMemberAndTeam(Member member, Team team) {
        return teamMemberRepository.existsByMemberAndTeam(member, team);
    }

    @Override
    public List<TeamMember> findByTeam(Team team) {
        return teamMemberRepository.findByTeam(team);
    }

    @Override
    public List<TeamMember> findByTeamAndPosition(Team team, String position) {
        return teamMemberRepository.findByTeamAndPosition(team, position);
    }

}