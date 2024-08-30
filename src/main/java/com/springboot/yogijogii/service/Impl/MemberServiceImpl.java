package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;

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

}
