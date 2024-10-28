package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.myPageDto.JoinTeamStatusDto;
import com.springboot.yogijogii.data.dto.myPageDto.MyPageTeamResponseDto;
import com.springboot.yogijogii.data.dto.myPageDto.UpdateTeamMemberRequestDto;
import com.springboot.yogijogii.data.dto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MyPageService {
    List<MyPageTeamResponseDto> getJoinedTeams(HttpServletRequest servletRequest);

    ResultDto updateTeamMember(Long teamId, UpdateTeamMemberRequestDto requestDto, HttpServletRequest servletRequest);

    List<JoinTeamStatusDto> getJoinRequests(HttpServletRequest servletRequest);

    ResultDto leaveTeam(HttpServletRequest servletRequest, Long teamId, String reason);
}
