package com.springboot.yogijogii.data.dto.fomationDto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FormationResponseDto {
    private Long id;
    private String positionName;
    List<FormationDetailResponseDto> formationDetailResponseDtoList;
}
