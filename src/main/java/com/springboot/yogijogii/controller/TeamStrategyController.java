package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;
import com.springboot.yogijogii.service.AdminTeamService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/team-strategy")
@Slf4j
public class TeamStrategyController {

    private final AdminTeamService adminTeamService;
//
//    @GetMapping("/get/team-member")
//    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
//    ResponseEntity<List<TeamMemberByPositionDto>>getTeamMemberByPosition(@RequestParam Long teamId, String position, HttpServletRequest servletRequest) {
//        List<TeamMemberByPositionDto> teamMemberByPositionDtos = adminTeamService.getTeamMemberByPosition(teamId, position, servletRequest);
//        return ResponseEntity.status(HttpStatus.OK).body(teamMemberByPositionDtos);
//    }

}
