package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.result.ResultStatusService;
import com.springboot.yogijogii.service.JoinTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JoinTeamServiceImpl implements JoinTeamService {
    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;
    private final TeamDao teamDao;
    private final JoinTeamDao joinTeamDao;
    private final TeamMemberDao teamMemberDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto joinTeam(HttpServletRequest servletRequest, JoinTeamDto requestDto, Long teamId) {

        ResultDto resultDto = new ResultDto();
        try {
            Member member = jwtAuthenticationService.authenticationToken(servletRequest);

            Team team = teamDao.findByTeamId(teamId);
            if (team == null) {
                resultDto.setSuccess(false);
                resultDto.setMsg("존재하지 않는 팀입니다.");
                return resultDto;
            }

            boolean isAlreadyMember = teamMemberDao.existsByMemberAndTeam(member, team);
            if (isAlreadyMember) {
                resultDto.setSuccess(false);
                resultDto.setMsg("이미 이 팀의 멤버로 소속되어 있습니다.");
                return resultDto;
            }

            boolean alreadyRequested = joinTeamDao.existsByTeamAndMemberAndStatus(team, member, "PENDING");
            if (alreadyRequested) {
                resultDto.setSuccess(false);
                resultDto.setMsg("이미 팀에 가입 신청을 한 상태입니다.");
                return resultDto;
            }

            JoinTeam joinTeam = JoinTeam.builder()
                    .team(team)
                    .member(member)
                    .name(requestDto.getName())
                    .address(requestDto.getAddress())
                    .gender(requestDto.getGender())
                    .level(requestDto.getLevel())
                    .hasExperience(requestDto.isHasExperience())
                    .joinReason(requestDto.getJoinReason())
                    .position(requestDto.getPosition())
                    .status("PENDING")
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();
            
            joinTeamDao.save(joinTeam);
            resultStatusService.setSuccess(resultDto);
            resultDto.setDetailMessage("팀 가입요청을 성공하였습니다.");
        } catch (Exception e) {
            resultStatusService.setSuccess(resultDto);
            resultDto.setDetailMessage("팀 가입 요청 실패: " + e.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto joinTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode, String position) {
        ResultDto resultDto = new ResultDto();

        try {
            Member member = jwtAuthenticationService.authenticationToken(servletRequest);

            Team team = teamDao.findByInviteCode(inviteCode);
            boolean isAlreadyMember = teamMemberDao.existsByMemberAndTeam(member, team);
            if (isAlreadyMember) {
                resultDto.setSuccess(false);
                resultDto.setMsg("이미 이 팀의 멤버로 소속되어 있습니다.");
                return resultDto;
            }
            if (team == null) {
                throw new IllegalArgumentException("유효하지 않은 초대 코드입니다.");
            }
            teamMemberDao.saveTeamMember(TeamMember.builder()
                    .member(member)
                    .team(team)
                    .role("ROLE_MEMBER")
                    .position(position)
                    .teamColor("#000000")
                    .createdDate(LocalDateTime.now())
                    .build());
            resultStatusService.setSuccess(resultDto);
            resultDto.setDetailMessage("초대코드로 팀가입에 성공하였습니다.");
        } catch (Exception e) {
            resultStatusService.setFail(resultDto);
            resultDto.setDetailMessage("초대코드로 팀가입에 실패하였습니다: " + e.getMessage());
        }
        return resultDto;
    }
}
