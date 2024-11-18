package com.springboot.yogijogii.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private LocalDateTime matchDayTime;

    @OneToOne
    private Formation formation;

}
