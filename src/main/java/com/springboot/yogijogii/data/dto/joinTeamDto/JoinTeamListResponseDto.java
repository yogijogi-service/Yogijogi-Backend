package com.springboot.yogijogii.data.dto.joinTeamDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinTeamListResponseDto {

    private Long joinTeamId;

    private String profileUrl;

    private String name;

    private String position;

}
