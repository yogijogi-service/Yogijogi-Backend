package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.service.AdminJoinTeamService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/admin/joinTeam")
public class AdminJoinTeamController {

    private final AdminJoinTeamService adminJoinTeamService;

    @GetMapping("/requestDetail/{requestId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<JoinTeamResponseDto> requestDetail(HttpServletRequest servletRequest, @PathVariable Long requestId) {
        JoinTeamResponseDto joinTeamResponseDto = adminJoinTeamService.requestDetail(servletRequest, requestId);
        return ResponseEntity.status(HttpStatus.OK).body(joinTeamResponseDto);
    }

    @GetMapping("/getPendingRequests/{teamId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<JoinTeamResponseDto>> getPendingRequests(HttpServletRequest servletRequest,
                                                                        @PathVariable Long teamId,
                                                                        @RequestParam(defaultValue = "전체") String position) {
        List<JoinTeamResponseDto> pendingRequests = adminJoinTeamService.getPendingRequests(servletRequest, teamId, position);
        return ResponseEntity.status(HttpStatus.OK).body(pendingRequests);
    }

    @PostMapping("/{teamId}/accept/{memberId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> acceptJoinRequest(HttpServletRequest servletRequest, @PathVariable Long teamId, @PathVariable Long memberId) throws Exception {
        // 팀 가입 수락
        ResultDto resultDto = adminJoinTeamService.processJoinRequest(servletRequest, teamId, memberId, true);
        return ResponseEntity.ok(resultDto);
    }

    @PostMapping("/{teamId}/deny/{memberId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> denyJoinRequest(HttpServletRequest servletRequest, @PathVariable Long teamId, @PathVariable Long memberId) throws Exception {
        // 팀 가입 거절
        ResultDto resultDto = adminJoinTeamService.processJoinRequest(servletRequest, teamId, memberId, false);
        return ResponseEntity.ok(resultDto);
    }
}
