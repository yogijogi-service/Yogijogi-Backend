package com.springboot.yogijogii.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String opposingTeam;

    private String matchAddress;

    private String matchStrategy;

    private String matchStartTime;

    private String matchEndTime;

    private LocalDate matchDay;

    @OneToOne
    private Formation formation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId") // 매니저를 참조하는 외래 키
    private Team team; // 매니저 정보 추가

    public TeamStrategy(String opposingTeam, String matchAddress, String matchStrategy, LocalDate matchDay, String matchStartTime,String matchEndTime ,Formation formation, Team team) {
        this.opposingTeam = opposingTeam;
        this.matchAddress = matchAddress;
        this.matchStrategy = matchStrategy;
        this.matchDay = matchDay;
        this.matchStartTime = matchStartTime;
        this.matchEndTime = matchEndTime;
        this.formation = formation;
        this.team = team;
    }

}
