package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterRequestDto;
import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterResponseDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.data.repository.team.TeamRepository;
import com.springboot.yogijogii.exception.TeamNotFoundException;
import com.springboot.yogijogii.exception.UnauthorizedException;
import com.springboot.yogijogii.jwt.Impl.JwtProvider;
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
                        team.getActivityTime(),
                        team.getActivityDays(),
                        team.getMatchLocation()
                ))
                .collect(Collectors.toList());
    }

}
