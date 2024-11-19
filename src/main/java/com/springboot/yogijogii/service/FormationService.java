package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.fomationDto.FormationRequestDto;
import com.springboot.yogijogii.data.dto.fomationDto.Formation_detailRequestDto;

import java.util.List;

public interface FormationService {
    ResultDto saveFormation(String formationName, List<Formation_detailRequestDto> formationRequestDto);
}
