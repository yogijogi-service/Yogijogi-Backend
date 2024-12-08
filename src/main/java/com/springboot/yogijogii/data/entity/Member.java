package com.springboot.yogijogii.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;  // id 식별값

    @Column(nullable = false,unique = true)
    private String email;

    private String password;

    private String passwordCheck;

    private String gender;

    private String name;

    private String address;

    private String addressDetail;

    private String profileUrl;

    private String birthDate;  //생년월일

    private String phoneNum;

    private String level;

    private boolean certificationNum;   // 인증번호

    private boolean hasExperience;  // 선수경험

    private String loginMethod;

    private boolean verified = false;

    private String serviceRole;

    private LocalDateTime create_At;

    private LocalDateTime update_At;

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.teamMembers.stream()
                .map(teamMember -> new SimpleGrantedAuthority(teamMember.getRole()))
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size=1)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<ServiceRole> serviceRoles = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Announcement> announcements = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberAgreement_id", referencedColumnName = "id")
    private MemberAgreement memberAgreement;

    public List<TeamMember> getMemberRolesWithInit() {
        Hibernate.initialize(teamMembers);
        return teamMembers;
    }

    public void addAuthAdditionalInfo(AdditionalInfoDto additionalInfoDto) {
        this.birthDate = additionalInfoDto.getBirthDate();
        this.level = additionalInfoDto.getLevel();
        this.hasExperience = additionalInfoDto.isHasExperience();
        this.gender = additionalInfoDto.getGender();
        this.address = additionalInfoDto.getAddress();
        this.update_At = LocalDateTime.now();
        this.addressDetail = additionalInfoDto.getAddressDetail();
    }
}
