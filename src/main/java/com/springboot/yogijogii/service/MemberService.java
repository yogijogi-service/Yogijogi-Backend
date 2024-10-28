package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.authDto.GoogleResponseDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.memberDto.MemberRequestDto;
import com.springboot.yogijogii.data.dto.memberDto.MemberResponseDto;
import com.springboot.yogijogii.data.dto.signDto.AgreementDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberAgreement;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

    Member createKakaoUser(KakaoResponseDto kakaoUserInfoResponse);
    Member createGoogleUser(GoogleResponseDto googleResponseDto);


    Member createUser(SignReqeustDto signReqeustDto);

    Member saveVerifyInfo(String phoneNum);

    MemberAgreement saveAgreement(AgreementDto agreementDto);

    MemberResponseDto getUser(HttpServletRequest servletRequest);

    ResultDto updateUser(HttpServletRequest servletRequest, MemberRequestDto requestDto);
}
