package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamListResponseDto;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.joinTeam.JoinTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinTeamDaoImpl implements JoinTeamDao {
    private final JoinTeamRepository joinTeamRepository;

    @Override
    public void save(JoinTeam joinTeam) {
        joinTeamRepository.save(joinTeam);
    }


    @Override
    public Optional<Object> findByTeamAndMember(Team team, Member requestedMember) {
        return joinTeamRepository.findByTeamAndMember(team, requestedMember);
    }

    @Override
    public void delete(JoinTeam joinRequest) {
        joinTeamRepository.delete(joinRequest);
    }

    @Override
    public JoinTeam findByJoinTeamId(Long joinTeamId) {
        JoinTeam joinTeam = joinTeamRepository.findJoinTeamByJoinTeamId(joinTeamId);
        return joinTeam;
    }

    @Override
    public boolean existsByTeamAndMemberAndStatus(Team team, Member member, String pending) {
        return joinTeamRepository.existsByTeamAndMemberAndStatus(team, member, pending);
    }

    @Override
    public List<JoinTeam> findByTeamAndStatusAndPosition(Team team, String pending, String position) {
        List<JoinTeam> joinTeamList = joinTeamRepository.findByTeamAndStatusAndPosition(team, pending, position);
        return joinTeamList;
    }

    @Override
    public List<JoinTeam> findByTeamAndStatus(Team team, String pending) {
        List<JoinTeam> joinTeamList = joinTeamRepository.findByTeamAndStatus(team, pending);
        return joinTeamList;
    }

    @Override
    public List<JoinTeam> findByStatusAndDate(String status, LocalDateTime date) {
        List<JoinTeam> joinTeamList = joinTeamRepository.findAllByStatusAndUpdatedDateBefore(status, date);
        return joinTeamList;
    }

    @Override
    public void deleteAll(List<JoinTeam> expiredRequests) {
        joinTeamRepository.deleteAll(expiredRequests);
    }

    @Override
    public List<JoinTeam> findByMember(Member member) {
        List<JoinTeam> joinTeamList = joinTeamRepository.findByMember(member);
        return joinTeamList;
    }
}
