package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTeamServiceImpl implements AdminTeamService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final TeamMemberDao teamMemberDao;
    private final TeamDao teamDao;

    @Override
    public ResultDto updateSubManagerRole(HttpServletRequest servletRequest, Long teamMemberId, boolean grant) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);

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


}


