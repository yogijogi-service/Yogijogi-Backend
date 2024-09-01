package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.teamDto.CreateTeamRquestDto;
import com.springboot.yogijogii.data.entity.Team;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface TeamService {
    Team saveTeamInfo(CreateTeamRquestDto createTeamRquestDto , MultipartFile image) throws IOException;
    ResultDto createTeam(CreateTeamRquestDto createTeamRquestDto, MultipartFile image,HttpServletRequest request) throws IOException;
}

