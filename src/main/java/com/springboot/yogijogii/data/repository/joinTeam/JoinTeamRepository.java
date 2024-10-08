package com.springboot.yogijogii.data.repository.joinTeam;

import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamDto;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamListResponseDto;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JoinTeamRepository extends JpaRepository<JoinTeam, Long> {

    Optional<Object> findByTeamAndMember(Team team, Member requestedMember);

    JoinTeam findJoinTeamByJoinTeamId(Long joinTeamId);

    boolean existsByTeamAndMemberAndStatus(Team team, Member member, String pending);

    List<JoinTeam> findByTeamAndStatusAndPosition(Team team, String pending, String position);

    List<JoinTeam> findByTeamAndStatus(Team team, String pending);

    List<JoinTeam> findAllByStatusAndUpdatedDateBefore(String status, LocalDateTime date);

    List<JoinTeam> findByMember(Member member);
}
