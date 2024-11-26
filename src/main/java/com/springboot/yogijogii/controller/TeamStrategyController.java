package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationNameResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.request.Formation_detailRequestDto;
import com.springboot.yogijogii.data.dto.teamStrategy.MatchStrategyDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamStrategyMonthlyDto;
import com.springboot.yogijogii.service.AdminTeamService;
import com.springboot.yogijogii.service.FormationService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/team-strategy")
@Slf4j
public class TeamStrategyController {

    private final AdminTeamService adminTeamService;
    private final FormationService formationService;

    @GetMapping("/get/team-member")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<TeamMemberByPositionDto>>getTeamMemberByPosition(@RequestParam Long teamId, @RequestParam  String position, HttpServletRequest servletRequest) {
        List<TeamMemberByPositionDto> teamMemberByPositionDtos = adminTeamService.getTeamMemberByPosition(teamId, position, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(teamMemberByPositionDtos);
    }


    @PostMapping("/save/formation")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> saveFormation(HttpServletRequest request,@RequestParam Long teamId,@RequestParam  String formationName ,@RequestBody List<Formation_detailRequestDto> formationDetailRequestDtos){
        ResultDto resultDto = formationService.saveFormation(request,teamId,formationName,formationDetailRequestDtos);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @DeleteMapping("/delete/formation")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto> deleteFormation(HttpServletRequest request, Long teamId, Long formationId){
        ResultDto resultDto = formationService.deleteFormation(request,teamId,formationId);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/get/formation")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FormationResponseDto> getFormation(HttpServletRequest request, @RequestParam Long teamId , @RequestParam  Long formationId){
        FormationResponseDto formationResponseDto = formationService.getFormation(request,teamId,formationId);
        return ResponseEntity.status(HttpStatus.OK).body(formationResponseDto);
    }
    @PostMapping("/save/team-strategy")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> saveMatchStrategy(HttpServletRequest request,@RequestBody MatchStrategyDto matchStrategyDto){
        ResultDto resultDto = adminTeamService.saveMatchStrategy(request,matchStrategyDto);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/get-position/name")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FormationNameResponseDto> getPositionListByName(HttpServletRequest request, Long teamId, String positionName) {
        FormationNameResponseDto formationNameResponseDto = formationService.getPositionListByName(request,teamId,positionName);
        return ResponseEntity.status(HttpStatus.OK).body(formationNameResponseDto);
    }
    @GetMapping("/get-strategy/monthly-day")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<TeamStrategyMonthlyDto>> getTeamStrategyMonthly(HttpServletRequest request, Long teamId,  @RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TeamStrategyMonthlyDto> teamStrategyMonthlyDtoList = adminTeamService.getTeamStrategyMonthly(request,teamId,date);
        return ResponseEntity.status(HttpStatus.OK).body(teamStrategyMonthlyDtoList);
    }

    @GetMapping("/get-strategy/monthly")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<TeamStrategyMonthlyDto>> getAllTeamStrategyMonthly(HttpServletRequest request, Long teamId,  @RequestParam String date) {
        List<TeamStrategyMonthlyDto> teamStrategyMonthlyDtoList = adminTeamService.getAllTeamStrategyMonthly(request,teamId,date);
        return ResponseEntity.status(HttpStatus.OK).body(teamStrategyMonthlyDtoList);
    }
}
