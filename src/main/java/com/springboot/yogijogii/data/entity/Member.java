package com.springboot.yogijogii.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
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


    private String password;


    private String gender;


    private String name;


    private String profileUrl;


    private String birthDate;  //생년월일


    private String phoneNum;


    private String level;


    private boolean certificationNum;   // 인증번호

    private boolean hasExperience;  // 선수경험

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> position;   //포지션

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> availableDays ;   //가능 요일

    private String availableTimeStart;  // 시작 시간

    private String  availableTimeEnd;  // 종료 시간

    boolean allAgreement;


    boolean consentServiceUser;


    boolean consentPersonalInfo;


    boolean consentToThirdPartyOffers;


    boolean consentToReceivingMail;

    private String loginMethod;

    private boolean verified = false;

    @Column(length = 512)
    private String refreshToken;

    private LocalDateTime create_At;

    private LocalDateTime update_At;

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

    public static Member createKakaoUser(KakaoResponseDto kakaoUserInfoResponse) {
        return Member.builder()
                .email(kakaoUserInfoResponse.getEmail())
                .name(kakaoUserInfoResponse.getName())
                .phoneNum(kakaoUserInfoResponse.getPhoneNum())
                .gender(kakaoUserInfoResponse.getGender())
                .birthDate(kakaoUserInfoResponse.getBirthDate())
                .profileUrl(kakaoUserInfoResponse.getProfileUrl())
                .loginMethod("Kakao")
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }

    public void addKakaoAdditionalInfo(AdditionalInfoDto additionalInfoDto) {
        this.level = additionalInfoDto.getLevel();
        this.hasExperience = additionalInfoDto.isHasExperience();
        this.update_At = LocalDateTime.now();
    }

}
