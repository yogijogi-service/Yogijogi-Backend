package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.FormationDao;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.fomationDto.FormationRequestDto;
import com.springboot.yogijogii.data.dto.fomationDto.Formation_detailRequestDto;
import com.springboot.yogijogii.data.entity.Formation;
import com.springboot.yogijogii.service.FormationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormationImpl implements FormationService {

    private final  FormationDao formationDao;

    @Override
    public ResultDto saveFormation(String formationName , List<Formation_detailRequestDto> formationDetailRequestDtos) {
        ResultDto resultDto = new ResultDto();
        formationDao.saveFormation(formationName,formationDetailRequestDtos);
        resultDto.setDetailMessage("포메이션 저장 완료!");
        return resultDto;
    }
}
