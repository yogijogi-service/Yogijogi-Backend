package com.springboot.yogijogii.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRole {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "memberId")
        private Member member;

        @Column(nullable = false)
        private String role;  // 예: ROLE_USER, ROLE_ADMIN

}
