package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dto.authDto.GoogleResponseDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.memberDto.MemberRequestDto;
import com.springboot.yogijogii.data.dto.memberDto.MemberResponseDto;
import com.springboot.yogijogii.data.dto.signDto.AgreementDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberAgreement;
import com.springboot.yogijogii.data.entity.MemberRole;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.memberRole.MemberRoleRepository;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;

    @Override
    public  Member createKakaoUser(KakaoResponseDto kakaoUserInfoResponse) {
        return Member.builder()
                .email(kakaoUserInfoResponse.getEmail())
                .name(kakaoUserInfoResponse.getName())
                .phoneNum(kakaoUserInfoResponse.getPhoneNum())
                .gender(kakaoUserInfoResponse.getGender())
                .birthDate(kakaoUserInfoResponse.getBirthDate())
                .profileUrl(kakaoUserInfoResponse.getProfileUrl())
                .loginMethod("Kakao")
                .password("pass")
                .passwordCheck("pass")
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }

    @Override
    public Member createGoogleUser(GoogleResponseDto googleResponseDto) {
        return Member.builder()
                .name(googleResponseDto.getName())
                .email(googleResponseDto.getEmail())
                .profileUrl(googleResponseDto.getProfileUrl())
                .loginMethod("Google")
                .password("pass")
                .passwordCheck("pass")
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }

    @Override
    public  Member createUser(SignReqeustDto signReqeustDto) {
        // 비밀번호 일치 여부 확인
        if (!signReqeustDto.getPassword().equals(signReqeustDto.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        return Member.builder()
                .email(signReqeustDto.getEmail())
                .password(passwordEncoder.encode(signReqeustDto.getPassword()))
                .passwordCheck(passwordEncoder.encode(signReqeustDto.getPasswordCheck()))
                .birthDate(signReqeustDto.getBirthDate())
                .level(signReqeustDto.getLevel())
                .hasExperience(signReqeustDto.isHasExperience())
                .certificationNum(true)
                .address(signReqeustDto.getBirthDate())
                .loginMethod("Normal")
                .gender(signReqeustDto.getGender())
                .name(signReqeustDto.getName())
                .address(signReqeustDto.getAddress())
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }
    @Override
    public  Member saveVerifyInfo(String phoneNum) {
        return Member.builder()
                .phoneNum(phoneNum)
                .certificationNum(true)
                .build();
    }

    public MemberAgreement saveAgreement(AgreementDto agreementDto) {
        return MemberAgreement.builder()
                .allAgreement(agreementDto.isAllAgreement())
                .consentPersonalInfo(agreementDto.isConsentPersonalInfo())
                .consentServiceUser(agreementDto.isConsentServiceUser())
                .consentToReceivingMail(agreementDto.isConsentToReceivingMail())
                .consentToThirdPartyOffers(agreementDto.isConsentToThirdPartyOffers())
                .build();

    }

    @Override
    public MemberResponseDto getUser(HttpServletRequest servletRequest) {
        String token = jwtProvider.resolveToken(servletRequest);
        String email = jwtProvider.getUsername(token);
        Member member = memberDao.findMemberByEmail(email);

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        if(jwtProvider.validToken(token)){
            memberResponseDto.setMemberId(member.getMemberId());
            memberResponseDto.setAddress(member.getAddress());
            memberResponseDto.setBirthDate(member.getBirthDate());
            memberResponseDto.setGender(member.getGender());
            memberResponseDto.setEmail(member.getEmail());
            memberResponseDto.setLevel(member.getLevel());
            memberResponseDto.setHasExperience(member.isHasExperience());
            memberResponseDto.setLoginMethod(member.getLoginMethod());
            memberResponseDto.setName(member.getName());
            memberResponseDto.setPhoneNum(member.getPhoneNum());
            memberResponseDto.setProfileUrl(member.getProfileUrl());
            memberResponseDto.setCreate_At(String.valueOf(member.getCreate_At()));
            memberResponseDto.setUpdate_At(String.valueOf(member.getUpdate_At()));
        }
        return memberResponseDto;
    }

    @Override
    public ResultDto updateUser(HttpServletRequest servletRequest, MemberRequestDto requestDto) {
        String token = jwtProvider.resolveToken(servletRequest);
        String email = jwtProvider.getUsername(token);
        Member member = memberDao.findMemberByEmail(email);
        ResultDto resultDto = new ResultDto();
        if(requestDto.getPassword().equals(requestDto.getPasswordCheck())){
            if(jwtProvider.validToken(token)){
                member.setPhoneNum(requestDto.getPhoneNum());
                member.setEmail(requestDto.getEmail());
                member.setPassword(passwordEncoder.encode(requestDto.getPassword()));
                member.setPasswordCheck(passwordEncoder.encode(requestDto.getPasswordCheck()));
                member.setBirthDate(requestDto.getBirtDate());
                member.setGender(requestDto.getGender());
                memberDao.save(member);
                resultDto.setSuccess(true);
                resultDto.setMsg("프로필 수정을 완료하였습니다.");
            }
        }else {
            resultDto.setSuccess(false);
            resultDto.setMsg("비밀번호가 일치하지 않습니다.");
        }
        return resultDto;
    }

    private void saveMemberRole(Member member, Team team , String role){
        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setTeam(team);
        memberRole.setRole(role);
    }
}
