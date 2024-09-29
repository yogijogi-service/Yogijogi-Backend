package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamListResponseDto;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import org.hibernate.mapping.Join;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JoinTeamDao {
    void save(JoinTeam joinTeam);

    Optional<Object> findByTeamAndMember(Team team, Member requestedMember);

    void delete(JoinTeam joinRequest);

    JoinTeam findByJoinTeamId(Long joinTeamId);

    boolean existsByTeamAndMemberAndStatus(Team team, Member member, String pending);

    List<JoinTeam> findByTeamAndStatusAndPosition(Team team, String pending, String position);

    List<JoinTeam> findByTeamAndStatus(Team team, String pending);

    List<JoinTeam> findByStatusAndDate(String status, LocalDateTime date);

    void deleteAll(List<JoinTeam> expiredRequests);

    List<JoinTeam> findByMember(Member member);
}
