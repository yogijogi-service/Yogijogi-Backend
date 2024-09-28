package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.myPageDto.MyPageTeamResponseDto;
import com.springboot.yogijogii.data.dto.myPageDto.UpdateMemberRoleRequestDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MyPageService {
    List<MyPageTeamResponseDto> getJoinedTeams(HttpServletRequest servletRequest);

    ResultDto updateMemberRole(Long teamId, UpdateMemberRoleRequestDto requestDto, HttpServletRequest servletRequest);
}
