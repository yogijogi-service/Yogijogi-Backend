package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.MemberRoleDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamDto;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberRole;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.jwt.Impl.JwtProvider;
import com.springboot.yogijogii.service.JoinTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class JoinTeamServiceImpl implements JoinTeamService {
    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;
    private final TeamDao teamDao;
    private final JoinTeamDao joinTeamDao;
    private final MemberRoleDao memberRoleDao;

    @Override
    public ResultDto joinTeam(HttpServletRequest servletRequest, JoinTeamDto requestDto, Long teamId) {

        ResultDto resultDto = new ResultDto();
        try {

            String token = jwtProvider.resolveToken(servletRequest);
            String email = jwtProvider.getUsername(token);
            Member member = memberDao.findMemberByEmail(email);

            Team team = teamDao.findByTeamId(teamId);
            if (team == null) {
                resultDto.setSuccess(false);
                resultDto.setMsg("존재하지 않는 팀입니다.");
                return resultDto;
            }

            boolean isAlreadyMember = memberRoleDao.existsByMemberAndTeam(member, team);
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

            JoinTeam joinTeam = new JoinTeam();
            joinTeam.setTeam(team);
            joinTeam.setMember(member);
            joinTeam.setName(requestDto.getName());
            joinTeam.setAddress(requestDto.getAddress());
            joinTeam.setGender(requestDto.getGender());
            joinTeam.setLevel(requestDto.getLevel());
            joinTeam.setHasExperience(requestDto.isHasExperience());
            joinTeam.setJoinReason(requestDto.getJoinReason());
            joinTeam.setPosition(requestDto.getPosition());
            joinTeam.setStatus("PENDING");

            joinTeamDao.save(joinTeam);
            resultDto.setSuccess(true);
            resultDto.setMsg("팀 가입요청을 성공하였습니다.");
        } catch (Exception e) {
            resultDto.setSuccess(false);
            resultDto.setMsg("팀 가입 요청 실패: " + e.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto joinTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode) {
        ResultDto resultDto = new ResultDto();

        try {
            String token = jwtProvider.resolveToken(servletRequest);
            String email = jwtProvider.getUsername(token);
            Member member = memberDao.findMemberByEmail(email);

            Team team = teamDao.findByInviteCode(inviteCode);
            if (team == null) {
                throw new IllegalArgumentException("유효하지 않은 초대 코드입니다.");
            }
            memberRoleDao.saveMemberRole(MemberRole.builder()
                    .member(member)
                    .team(team)
                    .role("Role_Member")
                    .build());
            resultDto.setSuccess(true);
            resultDto.setMsg("초대코드로 팀가입에 성공하였습니다.");
        } catch (Exception e) {
            resultDto.setSuccess(false);
            resultDto.setMsg("초대코드로 팀가입에 실패하였습니다: " + e.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto processJoinRequest(HttpServletRequest servletRequest,Long teamId, Long memberId, boolean accept) throws Exception {
        String token = jwtProvider.resolveToken(servletRequest);
        String email = jwtProvider.getUsername(token);
        Member loggedInMember = memberDao.findMemberByEmail(email);

        ResultDto resultDto = new ResultDto();

        // 요청된 팀 정보 조회
        Team team = teamDao.findByTeamId(teamId);

        // 매니저 권한 확인 (loggedInMember가 해당 팀의 ROLE_MANAGER 인지 확인)
        boolean isManager = memberRoleDao.existsByMemberAndTeamAndRole(loggedInMember, team, "Role_Manager");
        if (!isManager) {
            throw new IllegalAccessError("해당 팀을 관리할 권한이 없습니다.");
        }

        // 요청된 멤버 정보 조회
        Member requestedMember = memberDao.findMemberByID(memberId)
                .orElseThrow(() -> new IllegalArgumentException("요청된 멤버를 찾지 못했습니다"));

        // 팀 가입 요청 조회
        JoinTeam joinRequest = (JoinTeam) joinTeamDao.findByTeamAndMember(team, requestedMember)
                .orElseThrow(() -> new IllegalArgumentException("팀 가입 요청 실패"));

        if (accept) {
            // 팀 가입 수락: Role_Member로 추가
            MemberRole memberRole = new MemberRole();
            memberRole.setMember(requestedMember);
            memberRole.setTeam(team);
            memberRole.setRole("Role_Member");
            memberRoleDao.saveMemberRole(memberRole);

            // 가입 요청 삭제
            joinTeamDao.delete(joinRequest);

            resultDto.setMsg("팀가입 요청을 승인하였습니다.");
            resultDto.setSuccess(true);
        } else {
            // 팀 가입 거절: 요청만 삭제
            resultDto.setMsg("팀가입 요청을 거절하였습니다.");
            resultDto.setSuccess(false);
        }
        return resultDto;
    }

    @Override
    public JoinTeamResponseDto requestDetail(HttpServletRequest servletRequest, Long joinTeamId) {
        JoinTeamResponseDto joinTeamResponseDto = new JoinTeamResponseDto();
        try {
            String token = jwtProvider.resolveToken(servletRequest);
            String email = jwtProvider.getUsername(token);
            Member member = memberDao.findMemberByEmail(email);

            JoinTeam joinTeam = joinTeamDao.findByJoinTeamId(joinTeamId);

            boolean isManager = memberRoleDao.existsByMemberAndTeamAndRole(member, joinTeam.getTeam(), "Role_Manager");
            if (!isManager) {
                throw new IllegalAccessError("해당 팀을 관리할 권한이 없습니다.");
            }

            joinTeamResponseDto.setName(joinTeam.getName());
            joinTeamResponseDto.setGender(joinTeam.getGender());
            joinTeamResponseDto.setAddress(joinTeam.getAddress());
            joinTeamResponseDto.setLevel(joinTeam.getLevel());
            joinTeamResponseDto.setHasExperience(joinTeam.isHasExperience());
            joinTeamResponseDto.setPosition(joinTeam.getPosition());
            joinTeamResponseDto.setJoinReason(joinTeam.getJoinReason());
            joinTeamResponseDto.setMemberId(joinTeam.getMember().getMemberId());
            joinTeamResponseDto.setTeamId(joinTeam.getTeam().getTeamId());
        } catch (IllegalAccessError e) {
            throw new SecurityException("권한이 없습니다: " + e.getMessage());
        }
        return joinTeamResponseDto;
    }
}
