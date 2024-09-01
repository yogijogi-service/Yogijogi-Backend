package com.springboot.yogijogii.data.dto.teamDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateTeamRquestDto {

    private String teamName;

    private String team_introduce;

    private String region;

    private String town;

    private String matchLocation;

    private String dues;

    private List<String> activityDays;

    private List<String> activityTime;

    private String teamGender;

    private String ageRange;

    private String teamLevel;

    private List<String> positionRequired;
}
