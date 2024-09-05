package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.joinTeam.JoinTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
