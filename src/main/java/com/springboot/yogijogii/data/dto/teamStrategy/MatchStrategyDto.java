package com.springboot.yogijogii.data.dto.teamStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MatchStrategyDto {
    private String matchTime;
    private String opposingTeam;
    private String address;
    private String matchStrategy;
    private Long teamId;
    private Long formationId;
}
