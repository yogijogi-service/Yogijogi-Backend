package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamRequestDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.jwt.JwtProvider;
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
    @Override
    public ResultDto joinTeam(HttpServletRequest servletRequest, JoinTeamRequestDto requestDto, Long teamId) {

        ResultDto resultDto = new ResultDto();
        try {

            String token = jwtProvider.resolveToken(servletRequest);
            String email = jwtProvider.getUsername(token);
            Member member = memberDao.findMemberByEmail(email);

            Team team = teamDao.findByTeamId(teamId);

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
    public ResultDto joinTeamByInviteCode(JoinTeamService joinTeamService, String inviteCode) {
        return null;
    }
}
