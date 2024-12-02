package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.RefreshTokenResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface RefreshTokenService {
    RefreshTokenResponseDto reIssue(String refreshToken, HttpServletRequest request);

}
