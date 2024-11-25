package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationNameResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.request.Formation_detailRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FormationService {
    ResultDto saveFormation(HttpServletRequest request ,Long teamId,String formationName, List<Formation_detailRequestDto> formationRequestDto);
    FormationResponseDto getFormation(HttpServletRequest request, Long teamId, Long formationId);
    ResultDto deleteFormation(HttpServletRequest request, Long teamId, Long formationId);
    FormationNameResponseDto getPositionListByName(HttpServletRequest request, Long teamId,String positionName);
}
