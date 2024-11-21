package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.fomationDto.response.FormationResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.request.Formation_detailRequestDto;
import com.springboot.yogijogii.data.entity.Formation;

import java.util.List;

public interface FormationDao {
    void saveFormation(String formationName,List<Formation_detailRequestDto> formationRequestDto);
    FormationResponseDto getFormation(Long formationId);
    Formation findById(Long formationId);
}
