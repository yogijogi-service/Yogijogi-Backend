package com.springboot.yogijogii.data.dto.teamDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamResponseDto {

    private String teamName;

    private String team_introduce;

    private String teamImageUrl;

    private String inviteCode;

    private String region;

    private String town;

    private String matchLocation;

    private String dues;

    private Long memberCount;

    private List<String> activityDays;

    private List<String> activityTime;

    private String teamGender;

    private String ageRange;

    private String teamLevel;

    private List<String> positionRequired;
}
