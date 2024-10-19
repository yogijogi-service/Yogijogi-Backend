package com.springboot.yogijogii.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveTeamId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String profileUrl;

    private String birthDate;

    private String name;

    private String gender;

    private String address;

    private boolean hasExperience;  // 선수경험

    private String level;

    private String position;

    private String reason;

}
