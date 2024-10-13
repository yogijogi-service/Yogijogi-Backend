package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.service.JoinTeamService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/joinTeam")
@Slf4j
@RequiredArgsConstructor
public class JoinTeamController {

    private final JoinTeamService joinTeamService;

    @PostMapping("/{teamId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto> joinTeam(HttpServletRequest servletRequest, @RequestBody JoinTeamDto requestDto, @PathVariable Long teamId) {
        ResultDto resultDto = joinTeamService.joinTeam(servletRequest, requestDto, teamId);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @PostMapping("/inviteCode")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto> joinTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode, String position){
        ResultDto resultDto = joinTeamService.joinTeamByInviteCode(servletRequest, inviteCode, position);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }


}
