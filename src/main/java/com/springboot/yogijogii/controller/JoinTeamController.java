package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamRequestDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.service.JoinTeamService;
import com.springboot.yogijogii.service.MemberService;
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
    ResponseEntity<ResultDto> joinTeam(HttpServletRequest servletRequest, @RequestBody JoinTeamRequestDto requestDto, @PathVariable Long teamId) {
        ResultDto resultDto = joinTeamService.joinTeam(servletRequest, requestDto, teamId);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @PostMapping("/inviteCode")
    ResponseEntity<ResultDto> joinTeamByInviteCode(HttpServletRequest servletRequest, @RequestBody String inviteCode){
        ResultDto resultDto = joinTeamService.joinTeamByInviteCode(joinTeamService, inviteCode);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
}
