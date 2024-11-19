package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    SignInResultDto getKakaoUserInfo(String authorizeCode,HttpServletRequest request);
    SignInResultDto getGoogleUserInfo(String authorizeCode,HttpServletRequest request);
    SignInResultDto kakao_SignIn(String authorizeCode , HttpServletRequest request);
    SignInResultDto google_SignIn(String accessToken,HttpServletRequest request);
    ResultDto saveAdditionalInfo(AdditionalInfoDto additionalInfoDto, HttpServletRequest request);
    ResponseEntity<?> refreshAccessToken(String refreshToken);
}
