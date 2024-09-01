package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.teamDto.CreateTeamRquestDto;
import com.springboot.yogijogii.service.TeamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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

}
