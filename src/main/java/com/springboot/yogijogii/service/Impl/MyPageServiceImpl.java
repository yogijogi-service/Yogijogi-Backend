package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.dao.LeaveTeamDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dto.myPageDto.JoinTeamStatusDto;
import com.springboot.yogijogii.data.dto.myPageDto.MyPageTeamResponseDto;
import com.springboot.yogijogii.data.dto.myPageDto.UpdateTeamMemberRequestDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.entity.*;
import com.springboot.yogijogii.data.repository.teamMember.TeamMemberRepository;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final TeamMemberDao teamMemberDao;
    private final TeamDao teamDao;
    private final JoinTeamDao joinTeamDao;
    private final LeaveTeamDao leaveTeamDao;


    @Override
    public List<MyPageTeamResponseDto> getJoinedTeams(HttpServletRequest servletRequest) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);

        List<TeamMember> teamMembers = teamMemberDao.findByMember(member);
        log.info("teamMembers", teamMembers.toString());

        if(teamMembers.isEmpty()){
            throw new RuntimeException("소속된 팀이 없습니다.");
        }

        return teamMembers.stream()
                .filter(teamMember -> teamMember.getTeam()!=null)
                .map(teamMember -> new MyPageTeamResponseDto(
                        teamMember.getTeam().getTeamId(),
                        teamMember.getPosition(),
                        teamMember.getTeamColor(),
                        teamMember.getTeam().getTeamImageUrl(),
                        teamMember.getTeam().getTeamName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ResultDto updateTeamMember(Long teamId, UpdateTeamMemberRequestDto requestDto, HttpServletRequest servletRequest) {
        ResultDto resultDto = new ResultDto();
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);

        Team team = teamDao.findByTeamId(teamId);
        TeamMember teamMember = teamMemberDao.findByMemberAndTeam(member, team);

        teamMember.setPosition(requestDto.getPosition());
        teamMember.setTeamColor(requestDto.getTeamColor());

        teamMemberDao.save(teamMember);
        resultDto.setSuccess(true);
        resultDto.setMsg("팀 정보수정을 완료하였습니다.");
        return resultDto;
    }

    @Override
    public List<JoinTeamStatusDto> getJoinRequests(HttpServletRequest servletRequest) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);

        List<JoinTeam> joinRequests = joinTeamDao.findByMember(member);
        System.out.println("Join Requests: " + joinRequests); // 출력해서 값 확인

        return joinRequests.stream().map(joinRequest ->
                new JoinTeamStatusDto(
                        joinRequest.getTeam().getTeamName(),
                        joinRequest.getPosition(),
                        joinRequest.getStatus(),
                        joinRequest.getTeam().getTeamImageUrl()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public ResultDto leaveTeam(HttpServletRequest servletRequest, Long teamId, String reason) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);
        Team team = teamDao.findByTeamId(teamId);

        TeamMember teamMember = teamMemberDao.findByMemberAndTeam(member, team);
        LeaveTeam leaveTeam = LeaveTeam.builder()
                .team(team)
                .name(member.getName())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .address(member.getAddress())
                .hasExperience(member.isHasExperience())
                .level(member.getLevel())
                .position(teamMember.getPosition())
                .reason(reason)
                .build();
        leaveTeamDao.save(leaveTeam);
        teamMemberDao.delete(teamMember);
        ResultDto resultDto=new ResultDto();
        resultDto.setSuccess(true);
        resultDto.setMsg("팀 탈퇴를 성공하였습니다.");
        return resultDto;
    }
}
