package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamStrategy.MatchStrategyDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface AdminTeamService {
    ResultDto updateSubManagerRole(HttpServletRequest servletRequest, Long teamMemberId, boolean grant);

    List<TeamMemberByPositionDto> getTeamMemberByPosition(Long teamId,String position,HttpServletRequest servletRequest);
    ResultDto saveMatchStrategy(HttpServletRequest request, MatchStrategyDto matchStrategyDto);
    ResultDto grantMangerRole(HttpServletRequest servletRequest, Long teamMemberId);
}

