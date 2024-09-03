package com.springboot.yogijogii.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.springboot.yogijogii.data.dao.AuthDao;
import com.springboot.yogijogii.data.dao.MemberRoleDao;
import com.springboot.yogijogii.data.dto.CommonResponse;
import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.service.AuthService;
import com.springboot.yogijogii.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthDao authDao;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberRoleDao memberRoleDao;


    @Value("${kakao.client.id}")
    private String clientKey;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    @Value("${kakao.accesstoken.url}")
    private String kakaoAccessTokenUrl;

    @Value("${kakao.userinfo.url}")
    private String kakaoUserInfoUrl;

    @Override
    public ResponseEntity<?> getKakaoUserInfo(String authorizeCode) {
        log.info("[kakao login] issue a authorizeCode");
        ObjectMapper objectMapper = new ObjectMapper(); //json 파싱 객체
        RestTemplate restTemplate = new RestTemplate(); //client 연결 객체

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientKey);
        params.add("redirect_uri", redirectUrl);
        params.add("code", authorizeCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoAccessTokenUrl,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            log.info("[kakao login] authorizecode issued successfully");
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

            Object accessToken = responseMap.get("access_token");

            return ResponseEntity.ok(kakao_SignIn((String)accessToken));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get Kakao access token");
        }
    }

    private KakaoResponseDto getInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.POST, entity, String.class);

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseMap.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            KakaoResponseDto responseDto = KakaoResponseDto.builder()
                    .name((String) kakaoAccount.get("name"))
                    .phoneNum((String) kakaoAccount.get("phone_number"))
                    .email((String) kakaoAccount.get("email"))
                    .gender((String) kakaoAccount.get("gender"))
                    .birthDate((String) kakaoAccount.get("birthday"))
                    .profileUrl((String) profile.get("profile_image_url"))
                    .build();

            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SignInResultDto kakao_SignIn(String accessToken) {
        KakaoResponseDto kakaoUserInfoResponse = getInfo(accessToken);

        SignInResultDto signInResultDto = new SignInResultDto();
        if (kakaoUserInfoResponse == null) {
            return handleSignInFailure(signInResultDto, "Failed to get Kakao user info");
        }

        Member member = authDao.kakaoUserFind(kakaoUserInfoResponse.getEmail());

        if (member == null) {
            member = memberService.createKakaoUser(kakaoUserInfoResponse);
            authDao.KakaoMemberSave(member);
            addServiceRoleManager(member);
            setSuccess(signInResultDto);
            signInResultDto.setDetailMessage("회원가입 완료.");
        } else {
            setSuccess(signInResultDto);
            signInResultDto.setDetailMessage("로그인 성공.");
        }


        String accessTokenNew = jwtProvider.createToken(member.getEmail(), List.of("ROLE_USER"));

        String existingRefreshToken = member.getRefreshToken(); // 기존 Refresh Token 가져오기
        String refreshToken;

        if (existingRefreshToken != null && jwtProvider.validRefreshToken(existingRefreshToken)) {
            // 기존 Refresh Token이 유효하면 그대로 사용
            refreshToken = existingRefreshToken;
        } else {
            // 기존 Refresh Token이 없거나 만료되었으면 새로 발급
            refreshToken = jwtProvider.createRefreshToken(member.getEmail());
            member.setRefreshToken(refreshToken); // 새로운 Refresh Token을 DB에 저장 (필요할 경우)
            authDao.KakaoMemberSave(member); // 변경 사항 저장
        }

        signInResultDto.setToken(accessTokenNew);
        signInResultDto.setRefreshToken(refreshToken); // 최종 Refresh Token 설정

        setSuccess(signInResultDto);
        signInResultDto.setDetailMessage("로그인 성공.");
        log.info("[SignIn] SignInResultDto: {}", signInResultDto);

        return signInResultDto;

    }

    @Override
    public ResponseEntity<?> refreshAccessToken(String refreshToken) {
        if (jwtProvider.validRefreshToken(refreshToken)) {
            String email = jwtProvider.getUsernameFromRefreshToken(refreshToken);
            String newAccessToken = jwtProvider.createToken(email, List.of("ROLE_USER"));
            return ResponseEntity.ok(newAccessToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }
    }

    @Override
    public ResultDto kakao_additionalInfo(AdditionalInfoDto additionalInfoDto , HttpServletRequest request) {
        String info = jwtProvider.getUsername(request.getHeader("X-AUTH-TOKEN"));
        Member member = memberRepository.getByEmail(info);
        ResultDto resultDto = new ResultDto();

        if (member != null) {
            member.addKakaoAdditionalInfo(additionalInfoDto); // 기존 User 객체를 전달하여 새로운 User 객체 생성
            authDao.KakaoMemberSave(member);
            setSuccess(resultDto);
        } else {
            setFail(resultDto);
        }
        return resultDto;
    }

    private void setSuccess(ResultDto resultDto) {
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFail(ResultDto resultDto) {
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
    }
    private SignInResultDto handleSignInFailure(SignInResultDto signInResultDto, String errorMessage) {
        setFail(signInResultDto);
        signInResultDto.setDetailMessage(errorMessage);
        throw new RuntimeException(errorMessage);
    }
    private void addServiceRoleManager(Member member){
        ServiceRole serviceRole = new ServiceRole();
        serviceRole.setMember(member);
        serviceRole.setRole("Role_User");
        member.getServiceRoles().add(serviceRole);
        memberRoleDao.saveServiceRole(serviceRole);
        member.setServiceRole("Role_User");
        authDao.KakaoMemberSave(member);
    }
}
