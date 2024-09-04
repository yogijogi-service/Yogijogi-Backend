package com.springboot.yogijogii.data.dto.teamDto.search;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTeamFilterResponseDto {
    private String teamName;
    private String teamImageUrl;
    private String teamGender;
    private List<String> activityTime;
    private List<String> activityDays;
    private String matchLocation;
}
