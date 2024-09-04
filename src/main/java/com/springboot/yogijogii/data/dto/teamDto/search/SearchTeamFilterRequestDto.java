package com.springboot.yogijogii.data.dto.teamDto.search;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchTeamFilterRequestDto {
    private List<String> teamRegion;
    private List<String> teamLevel;
    private List<String> teamGender;
    private List<String> ageRange;
    private List<String> activityTime;
    private List<String> activityDays;
}
