package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;
import com.springboot.yogijogii.data.entity.Member;

import java.time.LocalDateTime;

public interface MemberService {

    Member createKakaoUser(KakaoResponseDto kakaoUserInfoResponse);

    Member createUser(SignReqeustDto signReqeustDto);

    Member saveVerifyInfo(String phoneNum);

}
