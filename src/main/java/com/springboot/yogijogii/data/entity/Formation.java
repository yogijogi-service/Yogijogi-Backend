package com.springboot.yogijogii.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormationDetail> details = new ArrayList<>(); // 초기화 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId") // 매니저를 참조하는 외래 키
    private Team team; // 매니저 정보 추가

    public void addFormationDetail(FormationDetail detail) {
        if (this.details == null) { // null 체크 추가
            this.details = new ArrayList<>();
        }
        this.details.add(detail);
        detail.setFormation(this); // 양방향 관계 설정
    }
}
