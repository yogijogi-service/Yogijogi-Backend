package com.springboot.yogijogii.controller;


import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.service.AuthService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.redirect.url}")
    private String googleRedirectUrl;


    @GetMapping("/kakao/get-url")
    public Map<String,String> getKakaoUrl(){
        Map<String, String> response = new HashMap<>();
        String url =  String.format(
                "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                kakaoClientId,kakaoRedirectUrl
        );
        response.put("kakaoURL", url);
        return  response;
    }
    @GetMapping("/google/get-url")
    public Map<String,String> getGoogleUrl(){
        Map<String, String> response = new HashMap<>();
        String url =  String.format(
                "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=email%%20profile",
                googleClientId, googleRedirectUrl
        );
        response.put("googleURL", url);
        return response;
    }
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> getKakaoAuthorizeCode(@RequestParam("code") String authorizeCode) {
        log.info("[kakao-login] Received authorizeCode: {}", authorizeCode);

        // 인가 코드를 클라이언트에 반환
        Map<String, String> response = new HashMap<>();
        response.put("authorizeCode", authorizeCode);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<?> getGoogleAuthorizeCode(@RequestParam("code") String authorizeCode) {
        log.info("[kakao-login] Received authorizeCode: {}", authorizeCode);

        // 인가 코드를 클라이언트에 반환
        Map<String, String> response = new HashMap<>();
        response.put("authorizeCode", authorizeCode);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/kakao/signin")
    public ResponseEntity<?> kakao_SignIn(@RequestParam String accessToken){
        log.info("[kakao-login] accessToken {}", accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(authService.getKakaoUserInfo(accessToken));
    }

    @PostMapping("/google/signin")
    public ResponseEntity<?> google_SignIn(@RequestParam String accessToken){
        log.info("[google-login] accessToken {}", accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(authService.getGoogleUserInfo(accessToken));
    }


    @PutMapping("/kakao/add-info")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> kakao_additionalInfo(AdditionalInfoDto additionalInfoDto , HttpServletRequest request){
        ResultDto resultDto = authService.kakao_additionalInfo(additionalInfoDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }

}
