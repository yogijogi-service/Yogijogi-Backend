package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamRequestDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface JoinTeamService {
    ResultDto joinTeam(HttpServletRequest servletRequest, JoinTeamRequestDto requestDto, Long teamId);

    ResultDto joinTeamByInviteCode(JoinTeamService joinTeamService, String inviteCode);
}
