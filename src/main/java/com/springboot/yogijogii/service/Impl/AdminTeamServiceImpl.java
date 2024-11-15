package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.service.AdminTeamService;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AdminTeamServiceImpl implements AdminTeamService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;
    private final TeamMemberDao teamMemberDao;

    @Override
    public ResultDto updateSubManagerRole(HttpServletRequest servletRequest, Long teamMemberId, boolean grant) {
        String token = jwtProvider.resolveToken(servletRequest);
        String email = jwtProvider.getUsername(token);
        Member member = memberDao.findMemberByEmail(email);

        ResultDto resultDto = new ResultDto();
        TeamMember teamMember = teamMemberDao.findById(teamMemberId);
        Team team = teamMember.getTeam();

        boolean isManager = teamMemberDao.existsByMemberAndTeamAndRole(member, team, "ROLE_MANAGER");
        if(!isManager){
            throw new RuntimeException("해당팀에 매니저가 아닙니다.");
        }
        if (teamMember.getRole().equals("ROLE_MANAGER")) {
            throw new IllegalArgumentException("매니저 권한을 삭제할 수 없습니다.");
        }


        // 부매니저 권한 부여 또는 해지
        if (grant) {
            teamMember.setRole("ROLE_SUBMANAGER");  // 부매니저 역할 설정
            resultDto.setSuccess(true);
            resultDto.setMsg("부매니저 권한이 부여되었습니다.");
        } else {
            teamMember.setRole("ROLE_MEMBER");  // 부매니저 권한 해지 시 기본 역할 설정
            resultDto.setSuccess(true);
            resultDto.setMsg("부매니저 권한이 해지되었습니다.");
        }
        teamMemberDao.save(teamMember);
        return resultDto;
    }

    @Override
    public ResultDto grantMangerRole(HttpServletRequest servletRequest, Long teamMemberId) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);
        TeamMember teamMember = teamMemberDao.findById(teamMemberId);

        Team team = teamMember.getTeam();
        TeamMember manager = teamMemberDao.findByMemberAndTeam(member, team);
        boolean isManager = teamMemberDao.existsByMemberAndTeamAndRole(member, team, "ROLE_MANAGER");

        if(!isManager){
            throw new RuntimeException("해당팀에 매니저가 아닙니다.");
        }
        if(!teamMember.getRole().equals("ROLE_SUBMANAGER")){
            throw new RuntimeException("오직 부매니저에게만 매니저 역할을 부여할 수 있습니다.");
        }
        teamMember.setRole("ROLE_MANAGER");
        manager.setRole("ROLE_SUBMANAGER");

        teamMemberDao.save(teamMember);
        teamMemberDao.save(manager);

        return new ResultDto(true, HttpStatus.OK.value(), "해당 맴버에게 매니저 역할을 부여하였고, 본인은 부매니저로 강등되었습니다.", "");
    }
}
