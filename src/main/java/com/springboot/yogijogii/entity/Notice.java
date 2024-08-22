package com.springboot.yogijogii.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private String title;

    private String content ;

    private String noticeImageUrl ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId") // Member 엔티티에서 Team 엔티티를 참조하는 외래 키
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId") // Member 엔티티에서 Team 엔티티를 참조하는 외래 키
    private Team team;

    private LocalDateTime create_At;

    private LocalDateTime update_At;


}
