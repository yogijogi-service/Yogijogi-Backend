package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.fomationDto.response.FormationNameResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.request.Formation_detailRequestDto;
import com.springboot.yogijogii.data.entity.Formation;
import com.springboot.yogijogii.data.entity.Team;

import java.util.List;
import java.util.Optional;

public interface FormationDao {
    void saveFormation(String formationName, Team team, List<Formation_detailRequestDto> formationRequestDto);
    FormationResponseDto getFormation(Long formationId);
    Formation findById(Long formationId);
    FormationNameResponseDto findByPositionName(Long teamId,String positionName);
    void deleteFormation(Long formationId);

}
