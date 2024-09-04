package com.springboot.yogijogii.data.dto.joinTeamDto;

import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinTeamRequestDto {

    private String name;

    private String gender;

    private String address;

    private String level;

    private boolean hasExperience;  // 선수경험

    private String position;

    private String joinReason;
}
