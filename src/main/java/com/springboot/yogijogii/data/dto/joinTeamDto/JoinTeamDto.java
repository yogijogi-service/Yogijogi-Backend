package com.springboot.yogijogii.data.dto.joinTeamDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinTeamDto {

    private String name;

    private String gender;

    private String address;

    private String level;

    private boolean hasExperience;  // 선수경험

    private String position;

    private String joinReason;
}
