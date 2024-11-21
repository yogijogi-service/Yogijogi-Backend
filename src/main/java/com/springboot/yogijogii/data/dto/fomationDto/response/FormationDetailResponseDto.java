package com.springboot.yogijogii.data.dto.fomationDto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FormationDetailResponseDto {
    private Long id;
    private String playerName;
    private String detailPosition;
    private double x;
    private double y;
}
