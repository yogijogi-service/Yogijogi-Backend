package com.springboot.yogijogii.data.dto.joinTeamDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinTeamResponseDto {

    private String name;

    private String gender;

    private String address;

    private String level;

    private boolean hasExperience;  // 선수경험

    private String position;

    private String joinReason;

    private Long memberId;

    private Long teamId;

    private String profileUrl;
}
