package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.fomationDto.FormationRequestDto;
import com.springboot.yogijogii.data.dto.fomationDto.Formation_detailRequestDto;
import com.springboot.yogijogii.data.entity.Formation;

import java.util.List;

public interface FormationDao {
    void saveFormation(String formationName,List<Formation_detailRequestDto> formationRequestDto);
}
