package com.springboot.yogijogii.data.dto.teamStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamMemberByPositionDto {
    private Long id;
    private String name;
    private String position;
}
