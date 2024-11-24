package com.springboot.yogijogii.data.dto.myPageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MyPageTeamResponseDto {
    private Long teamId;

    private String position;

    private String teamColor;

    private String teamImageUrl;

    private String teamName;

    private Boolean favoriteTeam;
}
