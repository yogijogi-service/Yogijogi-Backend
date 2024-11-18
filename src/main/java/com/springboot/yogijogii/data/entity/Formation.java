package com.springboot.yogijogii.data.entity;

import lombok.*;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
    private List<FormationDetail> details; // 포메이션의 세부 정보
}
