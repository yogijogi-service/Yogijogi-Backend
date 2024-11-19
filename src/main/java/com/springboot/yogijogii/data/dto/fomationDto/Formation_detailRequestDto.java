package com.springboot.yogijogii.data.dto.fomationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Formation_detailRequestDto {
    private Long playerId;
    private double x;
    private double y;

}
