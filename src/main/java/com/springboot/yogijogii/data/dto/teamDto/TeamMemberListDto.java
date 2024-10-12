package com.springboot.yogijogii.data.dto.teamDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamMemberListDto {
    private Long id;

    private String profileUrl;

    private String name;

    private String position;

    private String role;
}
