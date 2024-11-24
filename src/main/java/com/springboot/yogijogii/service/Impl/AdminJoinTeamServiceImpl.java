package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamListResponseDto;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.result.ResultStatusService;
import com.springboot.yogijogii.service.AdminJoinTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminJoinTeamServiceImpl implements AdminJoinTeamService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    private final MemberDao memberDao;
    private final TeamDao teamDao;
    private final JoinTeamDao joinTeamDao;
    private final TeamMemberDao teamMemberDao;

    @Override
    public ResultDto processJoinRequest(HttpServletRequest servletRequest, Long teamId, Long memberId, boolean accept) throws Exception {
        Member loggedInMember = jwtAuthenticationService.authenticationToken(servletRequest);
        ResultDto resultDto = new ResultDto();

        // 요청된 팀 정보 조회
        Team team = teamDao.findByTeamId(teamId);

        // 매니저 권한 확인 (loggedInMember가 해당 팀의 ROLE_MANAGER 인지 확인)
        boolean isManager = teamMemberDao.existsByMemberAndTeamAndRole(loggedInMember, team, "Role_Manager");
        if (!isManager) {
            throw new IllegalAccessError("해당 팀을 관리할 권한이 없습니다.");
        }

        // 요청된 멤버 정보 조회
        Member requestedMember = memberDao.findMemberByID(memberId)
                .orElseThrow(() -> new IllegalArgumentException("요청된 멤버를 찾지 못했습니다"));

        // 팀 가입 요청 조회
        JoinTeam joinRequest = (JoinTeam) joinTeamDao.findByTeamAndMember(team, requestedMember)
                .orElseThrow(() -> new IllegalArgumentException("팀 가입 요청 실패"));

        if(teamMemberDao.existsByMemberAndTeam(requestedMember,team)){
            throw new IllegalArgumentException("팀에 이미 가입된 사용자 입니다.");
        }

        if (accept) {
            // 가입 요청 삭제 대신 Status 변경
            joinRequest.setStatus("ACCEPT");
            // 팀 가입 수락: Role_Member로 추가
            TeamMember teamMember = TeamMember.builder()
                    .member(requestedMember)
                    .team(team)
                    .role("ROLE_MEMBER")
                    .position(joinRequest.getPosition())
                    .teamColor("#000000")
                    .favoriteTeam(false)
                    .createdDate(LocalDateTime.now())
                    .build();
            teamMemberDao.saveTeamMember(teamMember);
            resultStatusService.setSuccess(resultDto);
            resultDto.setDetailMessage("팀가입 요청을 승인하였습니다.");
        } else {
            joinRequest.setStatus("REJECT");
            resultStatusService.setFail(resultDto);
            resultDto.setDetailMessage("팀가입 요청을 거절하였습니다.");
        }
        joinRequest.setUpdatedDate(LocalDateTime.now());
        // Status 저장
        joinTeamDao.save(joinRequest);
        return resultDto;
    }

    @Override
    public JoinTeamResponseDto requestDetail(HttpServletRequest servletRequest, Long joinTeamId) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);
        JoinTeam joinTeam = joinTeamDao.findByJoinTeamId(joinTeamId);

        boolean isManager = teamMemberDao.existsByMemberAndTeamAndRole(member, joinTeam.getTeam(), "Role_Manager");
        if (!isManager) {
            throw new IllegalAccessError("해당 팀을 관리할 권한이 없습니다.");
        }

        JoinTeamResponseDto joinTeamResponseDto = JoinTeamResponseDto.builder()
                .name(joinTeam.getName())
                .gender(joinTeam.getGender())
                .address(joinTeam.getAddress())
                .level(joinTeam.getLevel())
                .hasExperience(joinTeam.isHasExperience())
                .position(joinTeam.getPosition())
                .joinReason(joinTeam.getJoinReason())
                .memberId(joinTeam.getMember().getMemberId())
                .teamId(joinTeam.getTeam().getTeamId())
                .profileUrl(joinTeam.getMember().getProfileUrl())
                .build();
        return joinTeamResponseDto;
    }

    @Override
    public List<JoinTeamListResponseDto> getPendingRequests(HttpServletRequest servletRequest, Long teamId, String position) {
        List<JoinTeam> joinRequests;

        Team team = teamDao.findByTeamId(teamId);

        if ("전체".equals(position)) {
            joinRequests = joinTeamDao.findByTeamAndStatus(team, "PENDING");
        } else {
            joinRequests = joinTeamDao.findByTeamAndStatusAndPosition(team, "PENDING", position);
        }

        return joinRequests.stream()
                .map(request -> JoinTeamListResponseDto.builder()
                        .joinTeamId(request.getJoinTeamId())
                        .profileUrl(request.getMember().getProfileUrl())
                        .name(request.getName())
                        .position(request.getPosition())
                        .build())
                .collect(Collectors.toList());
    }
}
