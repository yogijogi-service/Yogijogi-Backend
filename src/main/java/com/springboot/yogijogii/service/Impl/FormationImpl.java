package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.FormationDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationNameResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.request.Formation_detailRequestDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.result.ResultStatusService;
import com.springboot.yogijogii.service.FormationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormationImpl implements FormationService {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final FormationDao formationDao;
    private final TeamMemberDao teamMemberDao;
    private final ResultStatusService resultStatusService;


    @Override
    public ResultDto saveFormation(HttpServletRequest request ,Long teamId, String formationName , List<Formation_detailRequestDto> formationDetailRequestDtos) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        Long userId = member.getMemberId();
        Team team = teamMemberDao.findById(teamId).getTeam();
        boolean isManger = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if(member==null){
            throw new RuntimeException("회원 정보가 없습니다.");
        }
        if(!isManger){
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저권한이 없습니다.");
        }
        ResultDto resultDto = new ResultDto();
        formationDao.saveFormation(formationName,team,formationDetailRequestDtos);
        resultDto.setDetailMessage("포메이션 저장 완료!");
        resultStatusService.setSuccess(resultDto);
        return resultDto;
    }

    @Override
    public FormationResponseDto getFormation(HttpServletRequest request, Long teamId ,Long formationId) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        Long userId = member.getMemberId();
        boolean isManger = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if(!isManger){
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저권한이 없습니다.");
        }
        return formationDao.getFormation(formationId);
    }

    @Override
    public ResultDto deleteFormation(HttpServletRequest request, Long teamId, Long formationId) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();
        Long userId = member.getMemberId();
        boolean isManger = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if(!isManger){
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저권한이 없습니다.");
        }
        resultDto.setDetailMessage("포메이션 삭제 완료");
        resultStatusService.setSuccess(resultDto);
        formationDao.deleteFormation(formationId);

        return resultDto;
    }

    @Override
    public FormationNameResponseDto getPositionListByName(HttpServletRequest request, Long teamId,String positionName) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        Long userId = member.getMemberId();
        boolean isManger = teamMemberDao.isTeamMemberAndManager(userId, teamId);
        if(!isManger){
            throw new RuntimeException("해당팀에 속해있지 않거나 매니저권한이 없습니다.");
        }
        return formationDao.findByPositionName(teamId,positionName);
    }
}
