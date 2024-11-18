package com.springboot.yogijogii.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private int sequence;    // 순서 (위치 구분)

    private double x; // x 좌표 (예: 52.52734375)
    private double y; // y 좌표 (예: 369.73828125)

    private String color; // 색상 정보 (예: "var(--color-sk)")
    private String detailPosition; // 세부 포지션 (예: "ST")

    @ManyToOne
    private Formation formation; // 소속된 포메이션

    @OneToOne
    private TeamMember teamMember; // 해당 위치의 선수

}
