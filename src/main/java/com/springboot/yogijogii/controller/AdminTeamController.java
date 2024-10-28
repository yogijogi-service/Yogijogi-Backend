package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.service.AdminTeamService;
import com.springboot.yogijogii.service.TeamService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/team")
public class AdminTeamController {
    private final AdminTeamService adminTeamService;

    @PutMapping("/role")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> updateSubManagerRole(HttpServletRequest servletRequest,
                                                          @RequestParam Long teamMemberId,
                                                          @RequestParam boolean grant) {
        ResultDto resultDto = adminTeamService.updateSubManagerRole(servletRequest, teamMemberId, grant);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
}
