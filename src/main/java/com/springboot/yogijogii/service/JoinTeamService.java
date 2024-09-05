package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamDto;
import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface JoinTeamService {
    ResultDto joinTeam(HttpServletRequest servletRequest, JoinTeamDto requestDto, Long teamId);

    ResultDto joinTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode);

    ResultDto processJoinRequest(HttpServletRequest servletRequest, Long teamId, Long memberId, boolean accept) throws Exception;

    JoinTeamResponseDto requestDetail(HttpServletRequest servletRequest, Long requestId);
}
