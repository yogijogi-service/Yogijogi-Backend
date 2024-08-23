package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    ResponseEntity<?> getKakaoUserInfo(String authorizeCode);
    SignInResultDto kakao_SignIn(String authorizeCode);
    ResultDto kakao_additionalInfo(AdditionalInfoDto additionalInfoDto, HttpServletRequest request);
    ResponseEntity<?> refreshAccessToken(String refreshToken);
}
