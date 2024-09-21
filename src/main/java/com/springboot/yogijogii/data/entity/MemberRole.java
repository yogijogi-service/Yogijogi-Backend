package com.springboot.yogijogii.data.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    @Column(nullable = false)
    private String role;

    private String position;
}
