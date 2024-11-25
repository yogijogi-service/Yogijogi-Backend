package com.springboot.yogijogii.data.dto.teamStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MatchStrategyDto {
    private LocalDate matchDay;
    private String matchStartTime;
    private String matchEndTime;
    private String opposingTeam;
    private String address;
    private String matchStrategy;
    private Long teamId;
    private Long formationId;
}
