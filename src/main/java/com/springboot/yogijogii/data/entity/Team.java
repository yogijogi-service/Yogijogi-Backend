package com.springboot.yogijogii.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String teamImageUrl;

    @Column(nullable = false)
    private String team_introduce;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String town;

    @Column(nullable = false)
    private String matchLocation;

    @Column(nullable = false)
    private String dues;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<String> activityDays;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<String> activityTime;

    @Column(nullable = false)
    private String teamGender;

    @Column(nullable = false)
    private String ageRange;

    private String inviteCode;

    private String teamLevel;

    private LocalDateTime create_At;

    private LocalDateTime update_At;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<String> positionRequired;

    @BatchSize(size=10)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId") // 매니저를 참조하는 외래 키
    private Member member; // 매니저 정보 추가

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @BatchSize(size=10) // 예시로 설정한 값. 실제 적용에 맞게 변경
    @JsonIgnore
    private List<TeamMember> teamMembers;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<TeamStrategy> teamStrategyList;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formation> formationList;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Announcement> announcementList;

}
