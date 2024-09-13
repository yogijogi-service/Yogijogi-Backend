package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.joinTeamDto.JoinTeamResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdminJoinTeamService {
    ResultDto processJoinRequest(HttpServletRequest servletRequest, Long teamId, Long memberId, boolean accept) throws Exception;

    JoinTeamResponseDto requestDetail(HttpServletRequest servletRequest, Long joinTeamId);

    List<JoinTeamResponseDto> getPendingRequests(HttpServletRequest servletRequest, Long teamId, String position);
}
