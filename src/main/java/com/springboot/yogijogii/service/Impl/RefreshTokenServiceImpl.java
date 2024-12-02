package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dto.RefreshTokenResponseDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.data.repository.refreshToken.RefreshTokenRepository;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.refreshToken.RefreshToken;
import com.springboot.yogijogii.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberDao memberDao;

    @Override
    public RefreshTokenResponseDto reIssue(String refreshToken, HttpServletRequest request) {
        log.info("reIssue ==> refresh 토큰 통한 토큰 재발급 시작");

        // 유효기간 검증
        if(!jwtProvider.validRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("재로그인 필요");
        }
        log.info("reIssue ==> refresh 토큰 검증 성공");

        String email = jwtProvider.getUsernameFromRefreshToken(refreshToken);

        // 레디스에 그 유저에게 발급된 refresh토큰 있는지 확인
        RefreshToken findRefreshToken = refreshTokenRepository.findByUsername(email).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그아웃된 사용자"));
        log.info("reIssue ==> DB에 사용자 이름과 refresh 토큰 존재 확인");

        if(!findRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("redis의 RefreshToken과 일치 하지 않음");
        }
        log.info("reIssue ==> Redis의 RefreshToken과 일치 확인");

        Member member = memberDao.findMemberByEmail(email);
        if(member == null) {
            throw new IllegalArgumentException("존재 하지 않은 사용자 입니다.");
        }

        String newAccessToken = jwtProvider.createToken(
                String.valueOf(member.getEmail()),
                member.getTeamMembers().stream()
                        .map(TeamMember::getRole)
                        .collect(Collectors.toList())
        );

        String newRefreshToken = jwtProvider.createRefreshToken(email);
        findRefreshToken.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(findRefreshToken);

        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto(
                newAccessToken,newRefreshToken);


        return refreshTokenResponseDto;
    }
}
