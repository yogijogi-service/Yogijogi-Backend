package com.springboot.yogijogii.data.dto.fomationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FormationRequestDto {
    private String formationName;

    private List<Formation_detailRequestDto> formationDetailRequestDtos;


}

