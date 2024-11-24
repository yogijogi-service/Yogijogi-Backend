package com.springboot.yogijogii.controller;

import com.amazonaws.Response;
import com.springboot.yogijogii.data.dto.myPageDto.JoinTeamStatusDto;
import com.springboot.yogijogii.data.dto.myPageDto.MyPageTeamResponseDto;
import com.springboot.yogijogii.data.dto.myPageDto.UpdateTeamMemberRequestDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.service.MyPageService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/myPage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/teams")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<MyPageTeamResponseDto>> getJoinedTeams(HttpServletRequest servletRequest) {
        // 가입 중인 팀 목록 반환
        List<MyPageTeamResponseDto> myPageTeamResponseDtos = myPageService.getJoinedTeams(servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(myPageTeamResponseDtos);
    }

    @PutMapping("/favoriteTeam/{teamId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> checkFavoriteTeam(HttpServletRequest servletRequest,@PathVariable Long teamId) {
        ResultDto resultDto = myPageService.checkFavoriteTeam(servletRequest, teamId);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @PutMapping("/teamMember/{teamId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> updateTeamMember(@PathVariable Long teamId,
                                                      @RequestBody UpdateTeamMemberRequestDto requestDto,
                                                      HttpServletRequest servletRequest) {
        ResultDto resultDto = myPageService.updateTeamMember(teamId, requestDto, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/requests")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<JoinTeamStatusDto>> getJoinRequests(HttpServletRequest servletRequest) {
        List<JoinTeamStatusDto> joinRequests = myPageService.getJoinRequests(servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(joinRequests);
    }

    @DeleteMapping("/leave/{teamId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> leaveTeam(HttpServletRequest servletRequest,
                                               @PathVariable Long teamId,
                                               @RequestBody String reason) {
        ResultDto resultDto = myPageService.leaveTeam(servletRequest, teamId, reason);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
}
