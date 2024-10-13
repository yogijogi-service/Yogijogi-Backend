package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamDto;
import com.springboot.yogijogii.data.dto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface JoinTeamService {
    ResultDto joinTeam(HttpServletRequest servletRequest, JoinTeamDto requestDto, Long teamId);

    ResultDto joinTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode, String position);

}
