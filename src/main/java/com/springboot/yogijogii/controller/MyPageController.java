package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.myPageDto.JoinTeamStatusDto;
import com.springboot.yogijogii.data.dto.myPageDto.MyPageTeamResponseDto;
import com.springboot.yogijogii.data.dto.myPageDto.UpdateMemberRoleRequestDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
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

    @PutMapping("/member-role/team/{teamId}")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> updateMemberRole(@PathVariable Long teamId,
                                                      @RequestBody UpdateMemberRoleRequestDto requestDto,
                                                      HttpServletRequest servletRequest) {
        ResultDto resultDto = myPageService.updateMemberRole(teamId, requestDto, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/requests")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<JoinTeamStatusDto>> getJoinRequests(HttpServletRequest servletRequest) {
        List<JoinTeamStatusDto> joinRequests = myPageService.getJoinRequests(servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(joinRequests);
    }
}
