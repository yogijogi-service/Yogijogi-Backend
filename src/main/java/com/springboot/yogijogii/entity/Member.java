package com.springboot.yogijogii.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;  // id 식별값

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long birthDate;  //생년월일

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private boolean certificationNum;   // 인증번호

    @Column(nullable = true)
    private boolean hasExperience;  // 선수경험

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<String> position;   //포지션

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<String> availableDays ;   //가능 요일

    @Column(nullable = true)
    private String availableTimeStart;  // 시작 시간

    @Column(nullable = true)
    private String  availableTimeEnd;  // 종료 시간

    @Column(nullable = true)
    boolean allAgreement;

    @Column(nullable = false)
    boolean consentServiceUser;

    @Column(nullable = false)
    boolean consentPersonalInfo;

    @Column(nullable = false)
    boolean consentToThirdPartyOffers;

    @Column(nullable = true)
    boolean consentToReceivingMail;

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.memberRoles.stream()
                .map(memberRole -> new SimpleGrantedAuthority(memberRole.getRole()))
                .collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> joinTeam;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> createTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size=1)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<JoinForms> joinForms;

    public List<MemberRole> getMemberRolesWithInit() {
        Hibernate.initialize(memberRoles);
        return memberRoles;
    }

}
