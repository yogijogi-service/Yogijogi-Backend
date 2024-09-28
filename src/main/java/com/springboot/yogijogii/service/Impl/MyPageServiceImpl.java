package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dto.myPageDto.MyPageTeamResponseDto;
import com.springboot.yogijogii.data.dto.myPageDto.UpdateMemberRoleRequestDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberRole;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.memberRole.MemberRoleRepository;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MemberRoleRepository memberRoleRepository;
    private final TeamDao teamDao;

    @Override
    public List<MyPageTeamResponseDto> getJoinedTeams(HttpServletRequest servletRequest) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);

        List<MemberRole> memberRoles = memberRoleRepository.findByMember(member);
        log.info("memberRoles",memberRoles.toString());

        if(memberRoles.isEmpty()){
            throw new RuntimeException("소속된 팀이 없습니다.");
        }

        return memberRoles.stream()
                .filter(memberRole -> memberRole.getTeam()!=null)
                .map(memberRole -> new MyPageTeamResponseDto(
                        memberRole.getTeam().getTeamId(),
                        memberRole.getPosition(),
                        memberRole.getTeamColor(),
                        memberRole.getTeam().getTeamImageUrl(),
                        memberRole.getTeam().getTeamName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ResultDto updateMemberRole(Long teamId, UpdateMemberRoleRequestDto requestDto, HttpServletRequest servletRequest) {
        ResultDto resultDto = new ResultDto();
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);

        Team team = teamDao.findByTeamId(teamId);
        MemberRole memberRole = memberRoleRepository.findByMemberAndTeam(member, team);

        memberRole.setPosition(requestDto.getPosition());
        memberRole.setTeamColor(requestDto.getTeamColor());

        memberRoleRepository.save(memberRole);
        resultDto.setSuccess(true);
        resultDto.setMsg("팀 정보수정을 완료하였습니다.");
        return resultDto;
    }
}
