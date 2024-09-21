package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.teamDto.TeamResponseDto;
import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterRequestDto;
import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterResponseDto;
import com.springboot.yogijogii.service.SearchTeam;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchTeamController {

    private final SearchTeam searchTeam;

    @GetMapping("/join-team")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<SearchTeamFilterResponseDto>> searchJoinTeam(SearchTeamFilterRequestDto searchTeamFilterRequestDto, HttpServletRequest request) {
        List<SearchTeamFilterResponseDto> teams = searchTeam.searchJoinTeam(searchTeamFilterRequestDto,request);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/teamByInviteCode")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<TeamResponseDto> searchTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode) {
        TeamResponseDto teamResponseDto = searchTeam.searchTeamByInviteCode(servletRequest, inviteCode);
        return ResponseEntity.status(HttpStatus.OK).body(teamResponseDto);
    }


}
