package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    SignInResultDto getKakaoUserInfo(String authorizeCode);
    SignInResultDto getGoogleUserInfo(String authorizeCode);
    SignInResultDto kakao_SignIn(String authorizeCode);
    SignInResultDto google_SignIn(String accessToken);
    ResultDto kakao_additionalInfo(AdditionalInfoDto additionalInfoDto, HttpServletRequest request);
    ResponseEntity<?> refreshAccessToken(String refreshToken);
}
