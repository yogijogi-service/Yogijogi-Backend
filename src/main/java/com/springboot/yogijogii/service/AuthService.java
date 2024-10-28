package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    SignInResultDto getKakaoAccessToken(String authorizeCode);
    SignInResultDto getGoogleAccessToken(String authorizeCode);
    SignInResultDto kakao_SignIn(String authorizeCode);
    SignInResultDto google_SignIn(String accessToken);
    ResultDto auth_additionalInfo(AdditionalInfoDto additionalInfoDto, HttpServletRequest request);
    ResponseEntity<?> refreshAccessToken(String refreshToken);
}
