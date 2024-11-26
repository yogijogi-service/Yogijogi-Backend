package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dto.teamDto.TeamResponseDto;
import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterRequestDto;
import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterResponseDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.data.repository.team.TeamRepository;
import com.springboot.yogijogii.exception.TeamNotFoundException;
import com.springboot.yogijogii.exception.UnauthorizedException;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.service.SearchTeam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchTeamImpl implements SearchTeam {
    private final JwtProvider jwtProvider;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<SearchTeamFilterResponseDto> searchJoinTeam(SearchTeamFilterRequestDto searchTeamFilterRequestDto, HttpServletRequest request) {

        String token = jwtProvider.resolveToken(request);

        String email = jwtProvider.getUsername(token);
        if (email == null) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        Member member = memberRepository.getByEmail(email);
        if (member == null) {
            throw new UnauthorizedException("사용자를 찾을 수 없습니다.");
        }

        List<Team> teams = teamRepository.searchTeams(searchTeamFilterRequestDto);
        if (teams.isEmpty()) {
            throw new TeamNotFoundException("검색한 팀이 존재하지 않습니다.");
        }

        return teams.stream()
                .map(team -> new SearchTeamFilterResponseDto(
                        team.getTeamName(),
                        team.getTeamImageUrl(),
                        team.getTeamGender(),
                        team.getTeamMembers().stream().count(),
                        team.getActivityTime(),
                        team.getActivityDays(),
                        team.getMatchLocation()
                ))
                .collect(Collectors.toList());
    }

    public TeamResponseDto searchTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode) {
        // 초대 코드로 팀을 찾기
        Team team = teamRepository.findByInviteCode(inviteCode);

        if (team == null) {
            log.error("팀을 찾을 수 없습니다. inviteCode: {}", inviteCode);
            throw new IllegalArgumentException("해당 초대 코드로 팀을 찾을 수 없습니다.");
        }

        // 팀 정보를 TeamResponseDto로 변환하여 반환
        TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                .teamName(team.getTeamName())
                .team_introduce(team.getTeam_introduce())
                .teamImageUrl(team.getTeamImageUrl())
                .inviteCode(team.getInviteCode())
                .region(team.getRegion())
                .town(team.getTown())
                .matchLocation(team.getMatchLocation())
                .dues(team.getDues())
                .memberCount(team.getTeamMembers().stream().count())
                .activityDays(team.getActivityDays())
                .activityTime(team.getActivityTime())
                .teamGender(team.getTeamGender())
                .ageRange(team.getAgeRange())
                .teamLevel(team.getTeamLevel())
                .positionRequired(team.getPositionRequired())
                .build();

        return teamResponseDto;
    }

}
