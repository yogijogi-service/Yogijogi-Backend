package com.springboot.yogijogii.data.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinTeamId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    private String gender;

    private String address;

    private String level;

    private boolean hasExperience;  // 선수경험

    private String position;

    private String joinReason;

    private String status;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
