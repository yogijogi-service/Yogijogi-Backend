package com.springboot.yogijogii.data.entity;

import jdk.jfr.Enabled;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId") // 매니저를 참조하는 외래 키
    private Team team; // 매니저 정보 추가

    public Announcement(String title, String content, String imageUrl, Member member,Team team) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
        this.member = member;
        this.team = team;
    }
}
