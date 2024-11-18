package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.member.ServiceRoleRepository;
import com.springboot.yogijogii.data.repository.teamMember.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.springboot.yogijogii.data.entity.QTeam.team;

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
    public List<TeamMember> findByMember(Member member) {
        return teamMemberRepository.findByMember(member);
    }

    @Override
    public List<TeamMember> findByTeam(Team team, Sort sortOrder) {
        return teamMemberRepository.findByTeam(team, sortOrder);
    }

    @Override
    public List<TeamMember> findByTeamAndPosition(Team team, String position, Sort sortOrder) {
        return teamMemberRepository.findByTeamAndPosition(team, position, sortOrder);
    }

    @Override
    public TeamMember findById(Long teamMemberId) {
        return teamMemberRepository.findTeamMemberById(teamMemberId);
    }

    @Override
    public TeamMember findByMemberAndTeam(Member member, Team team){
        return teamMemberRepository.findTeamMemberByMemberAndTeam(member, team);
    }

    @Override
    public void delete(TeamMember teamMember) {
        teamMemberRepository.delete(teamMember);
    }

    @Override
    public void save(TeamMember teamMember) {
        teamMemberRepository.save(teamMember);
    }




}
