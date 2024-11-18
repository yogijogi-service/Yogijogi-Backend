package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamStrategy.TeamMemberByPositionDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdminTeamService {
    ResultDto updateSubManagerRole(HttpServletRequest servletRequest, Long teamMemberId, boolean grant);

}

