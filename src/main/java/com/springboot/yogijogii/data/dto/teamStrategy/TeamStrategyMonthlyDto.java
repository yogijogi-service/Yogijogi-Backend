package com.springboot.yogijogii.data.dto.teamStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamStrategyMonthlyDto {
    private Long id;

    private String team;

    private String opposingTeam;

    private String matchStartTime;

    private String matchEndTime;
}
