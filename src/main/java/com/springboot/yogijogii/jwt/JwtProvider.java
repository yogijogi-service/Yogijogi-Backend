package com.springboot.yogijogii.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);


    @Value("${springboot.jwt.secret}")  // 시크릿 키
    private String secretKey = "asdqwe12321s@!#"; // 임시 시크릿 키


    @Value("${springboot.jwt.refresh-secret}")
    private String refreshSecretKey;
    private final long tokenValidTime = 1000*60*60;
    private final long refreshTokenValidTime = 1000 * 60 * 60 * 24 * 7; // 7일



    //시크릿 키 초기화
    @PostConstruct
    protected void init(){
        logger.info(" [init] 시크릿 키 초기화 시작 ");
        System.out.println("secretKey : "+secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        System.out.println(secretKey);
        logger.info("[init] 시크릿 키 초기화 완료 : {} " , secretKey);

    }

    //jwt 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token){
        logger.info("[getAuthentication] 토큰 인증 정보 조회 시작");

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        logger.info("[getAuthenticaiton] 토큰 인증 정보 조회 완료, UserDetails userName",userDetails.getUsername());

        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

    }

    // jwt토큰 생성
    public String createToken(String email, List<String> roles){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles",roles);

        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() +tokenValidTime))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        logger.info("[createToken] : {} " , token );
        return token;
    }

    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    // jwt 토큰에서 회원 구별 조회
    public String getUsername(String token){
        logger.info("[getUsername] 회원 구별 조회 시작");

        String info = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody() // 토큰 본문
                .getSubject(); // 토큰 본문에서 Subject -> 회원 구별

        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {} ", info);

        return info;
    }
    public String getUsernameFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(refreshSecretKey)
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
    }

    public long getExpirationDuration(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(refreshSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    // HTTP 헤더 정보에 X-AUTH-TOKEN 전달
    public String resolveToken(HttpServletRequest request){
        logger.info("[resolveToken] : {} " , request);
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;


        }
    }

    public boolean validRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
