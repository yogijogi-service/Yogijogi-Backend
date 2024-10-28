package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.teamDto.TeamResponseDto;
import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterRequestDto;
import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SearchTeam {
    List<SearchTeamFilterResponseDto> searchJoinTeam(SearchTeamFilterRequestDto searchTeamFilterRequestDto, HttpServletRequest request);

    TeamResponseDto searchTeamByInviteCode(HttpServletRequest servletRequest, String inviteCode);
}
