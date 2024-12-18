package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.*;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamStrategy.MatchStrategyDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamStrategyMonthlyDto;
import com.springboot.yogijogii.data.entity.*;
import com.springboot.yogijogii.dateConverter.DateTimeConverter;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.result.ResultStatusService;
import com.springboot.yogijogii.service.AdminTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminTeamServiceImpl implements AdminTeamService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final TeamMemberDao teamMemberDao;
    private final TeamStrategyDao teamStrategyDao;
    private final ResultStatusService resultStatusService;
    private final FormationDao formationDao;
    private final TeamDao teamDao;
//    private final DateTimeConverter dateTimeConverter;

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
        resultStatusService.setSuccess(resultDto);
        // 부매니저 권한 부여 또는 해지
        if (grant) {
            teamMember.setRole("ROLE_SUBMANAGER");  // 부매니저 역할 설정
            resultDto.setDetailMessage("부매니저 권한이 부여되었습니다.");
        } else {
            teamMember.setRole("ROLE_MEMBER");  // 부매니저 권한 해지 시 기본 역할 설정
            resultDto.setDetailMessage("부매니저 권한이 해지되었습니다.");
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

        ResultDto resultDto = new ResultDto();
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

        resultStatusService.setSuccess(resultDto);
        resultDto.setDetailMessage("해당 맴버에게 매니저 역할을 부여하였고, 본인은 부매니저로 강등되었습니다.");

        return resultDto;
    }

    @Override
    public List<TeamMemberByPositionDto> getTeamMemberByPosition(Long teamId, String position, HttpServletRequest request) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        Long userId = member.getMemberId();
        boolean isManger = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if(!isManger){
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저권한이 없습니다.");
        }
        return teamMemberDao.getTeamMemberByUserIdAndPosition(teamId, position);
    }
    @Override
    public ResultDto saveMatchStrategy(HttpServletRequest request, MatchStrategyDto matchStrategyDto) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();

        Long userId = member.getMemberId();
        Long teamId = matchStrategyDto.getTeamId();

        // 매니저 권한 확인
        boolean isManager = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if (!isManager) {
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저 권한이 없습니다.");
        }

        // Formation 및 Team 가져오기
        Formation formation = formationDao.findById(matchStrategyDto.getFormationId());
        Team team = teamDao.findByTeamId(matchStrategyDto.getTeamId());

        // TeamStrategy 생성 및 저장
        TeamStrategy teamStrategy = new TeamStrategy(
                matchStrategyDto.getOpposingTeam(),
                matchStrategyDto.getAddress(),
                matchStrategyDto.getMatchStrategy(),
                matchStrategyDto.getMatchDay(),
                matchStrategyDto.getMatchStartTime(),
                matchStrategyDto.getMatchEndTime(),
                formation,
                team
        );

        teamStrategyDao.saveMatchStrategy(teamStrategy); // 저장 로직

        resultDto.setDetailMessage("경기 전략 저장");
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }

    @Override
    public List<TeamStrategyMonthlyDto> getTeamStrategyMonthly(HttpServletRequest request, Long teamId,LocalDate date) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        Long userId = member.getMemberId();

        // 매니저 권한 확인
        boolean isManager = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if (!isManager) {
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저 권한이 없습니다.");
        }

        return teamStrategyDao.findTeamStrategyMonthlyInfo(teamId,date);
    }

    @Override
    public List<TeamStrategyMonthlyDto> getAllTeamStrategyMonthly(HttpServletRequest request, Long teamId, String date) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        Long userId = member.getMemberId();

        // 매니저 권한 확인
        boolean isManager = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if (!isManager) {
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저 권한이 없습니다.");
        }
        return teamStrategyDao.findAllTeamStrategyMonthlyInfo(teamId,date);
    }
}


