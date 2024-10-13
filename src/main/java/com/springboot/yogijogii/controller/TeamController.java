package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamDto.CreateTeamRquestDto;
import com.springboot.yogijogii.data.dto.teamDto.TeamMemberListDto;
import com.springboot.yogijogii.data.dto.teamDto.TeamResponseDto;
import com.springboot.yogijogii.service.TeamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/create")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto> createTeam(CreateTeamRquestDto createTeamRquestDto, MultipartFile image, HttpServletRequest request) throws IOException{
        ResultDto resultDto = teamService.createTeam(createTeamRquestDto, image, request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/{teamId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<TeamResponseDto> getTeam(HttpServletRequest servletRequest,
                                            @PathVariable Long teamId) {
        TeamResponseDto teamResponseDto = teamService.getTeam(servletRequest, teamId);
        return ResponseEntity.status(HttpStatus.OK).body(teamResponseDto);
    }

    @GetMapping("/{teamId}/memberList")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<List<TeamMemberListDto>> getTeamMemberList(HttpServletRequest servletRequest,
                                                              @PathVariable Long teamId,
                                                              @ApiParam(value = "포지션", allowableValues = "전체, 공격수, 수비수, 미드필더, 골키퍼", required = true)
                                                              @RequestParam(defaultValue = "전체") String position,
                                                              @ApiParam(value = "정렬", allowableValues = "최신 가입순, 오래된 가입순", required = true)
                                                              @RequestParam(defaultValue = "최신 가입순") String sort) {

        List<TeamMemberListDto> teamMemberListDtos = teamService.getTeamMemberList(servletRequest, teamId, position, sort);
        return ResponseEntity.status(HttpStatus.OK).body(teamMemberListDtos);
    }

}
