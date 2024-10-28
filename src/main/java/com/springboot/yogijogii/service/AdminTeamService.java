package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface AdminTeamService {
    ResultDto updateSubManagerRole(HttpServletRequest servletRequest, Long teamMemberId, boolean grant);
}
