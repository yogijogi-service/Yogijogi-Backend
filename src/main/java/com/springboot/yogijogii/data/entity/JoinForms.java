package com.springboot.yogijogii.data.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinForms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String joinStatus = "대기중";  // 가입 상태

    @Column(nullable = false)
    private String position; // 추가 전달 사항

    @Column(nullable = false)
    private String additionalInfo; // 추가 전달 사항

    @Column(nullable = false)
    private String withdrawalInfo; // 추가 전달 사항

    @Column(nullable = false)
    private boolean  checked  = false; // 추가 전달 사항


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;
}
