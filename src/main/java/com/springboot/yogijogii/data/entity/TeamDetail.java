package com.springboot.yogijogii.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamDetailId;

    private String matchDate;

    private String matchTime;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private String  opponentTeam;

    private String matchLocation;

    private String matchTactics;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private Map<Double,Double> formation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

}
