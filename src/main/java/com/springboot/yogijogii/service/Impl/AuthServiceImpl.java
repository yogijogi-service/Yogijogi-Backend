package com.springboot.yogijogii.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.springboot.yogijogii.Result.ResultStatusService;
import com.springboot.yogijogii.data.dao.AuthDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.authDto.GoogleResponseDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.ResultDto;
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
    private final TeamMemberDao teamMemberDao;
    private final ResultStatusService resultStatusService;


    @Value("${kakao.client.id}")
    private String clientKey;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    @Value("${kakao.accesstoken.url}")
    private String kakaoAccessTokenUrl;

    @Value("${kakao.userinfo.url}")
    private String kakaoUserInfoUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect.url}")
    private String googleRedirectUrl;

    @Value("${google.userinfo.url}")
    private String googleUserInfoUrl;

    @Value("${google.accesstoken.url}")
    private String googleAccessTokenUrl;


    @Override
    public SignInResultDto getKakaoUserInfo(String authorizeCode) {
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

            return kakao_SignIn((String)accessToken);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SignInResultDto getGoogleUserInfo(String authorizeCode) {
        log.info("[google login] issue a authorizeCode: {}", authorizeCode);
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUrl);
        params.add("code", authorizeCode);

        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, httpHeaders);
        log.info("[google login] Google Access Token URL: {}", googleAccessTokenUrl);
        log.info("[google login] Request Headers: {}", httpHeaders);
        log.info("[google login] Request Parameters: {}", params);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    googleAccessTokenUrl,
                    HttpMethod.POST,
                    googleTokenRequest,
                    String.class
            );
            log.info("[google login] authorizecode issued successfully");
            log.info("Response: {}", response.getBody()); // 응답 본문 출력
            Map<String,Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            log.info("[Response Map] : {}", responseMap);

            String accessToken = (String) responseMap.get("access_token");
            log.info("[accessToken] : {}", accessToken);

            return google_SignIn(accessToken);

        } catch (Exception e) {
            log.error("An error occurred while fetching Google access token", e);
            e.printStackTrace();
            return null;
        }
    }

    private KakaoResponseDto getKakaoInfo(String accessToken) {
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

            // 전화번호 변환
            String rawPhoneNumber = (String) kakaoAccount.get("phone_number");
            String formattedPhoneNumber = formatPhoneNumber(rawPhoneNumber);

            KakaoResponseDto responseDto = KakaoResponseDto.builder()
                    .name((String) kakaoAccount.get("name"))
                    .email((String) kakaoAccount.get("email"))
                    .phoneNum(formattedPhoneNumber)
                    .build();

            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private GoogleResponseDto getGoogleInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders httpHeaders = new HttpHeaders();
        log.info("[accessToken] : {}", accessToken);

        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(googleUserInfoUrl, HttpMethod.GET, entity, String.class);

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            log.info("[Google User Info Response] : {}", responseMap);

            // emailAddresses 리스트에서 이메일 추출
            List<Map<String, Object>> emailAddresses = (List<Map<String, Object>>) responseMap.get("emailAddresses");
            String email = null;
            if (emailAddresses != null && !emailAddresses.isEmpty()) {
                email = (String) emailAddresses.get(0).get("value");
            }

            if (email == null) {
                log.error("Google 계정에서 이메일을 제공하지 않았습니다.");
                return null;
            }

            GoogleResponseDto googleResponseDto = GoogleResponseDto.builder()
                    .name((String) ((List<Map<String, Object>>) responseMap.get("names")).get(0).get("displayName"))
                    .email(email)
                    .build();

            return googleResponseDto;

        } catch (Exception e) {
            log.error("Google 사용자 정보 조회 중 오류 발생", e);
            return null;
        }
    }

    @Override
    public SignInResultDto kakao_SignIn(String accessToken) {
        KakaoResponseDto kakaoUserInfoResponse = getKakaoInfo(accessToken);

        SignInResultDto signInResultDto = new SignInResultDto();
        if (kakaoUserInfoResponse == null) {
            return handleSignInFailure(signInResultDto, "Failed to get Kakao user info");
        }

        Member member = authDao.findMember(kakaoUserInfoResponse.getEmail());

        if (member == null) {
            member = memberService.createKakaoUser(kakaoUserInfoResponse);
            member.setServiceRole("ROLE_USER");
            authDao.saveMember(member);
            addServiceRoleManager(member);
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setNewUser(true);
            signInResultDto.setDetailMessage("회원가입 완료.");
        } else {
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setNewUser(false);
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
            authDao.saveMember(member); // 변경 사항 저장
        }

        signInResultDto.setToken(accessTokenNew);
        signInResultDto.setRefreshToken(refreshToken); // 최종 Refresh Token 설정

        resultStatusService.setSuccess(signInResultDto);
        signInResultDto.setDetailMessage("로그인 성공.");
        log.info("[SignIn] SignInResultDto: {}", signInResultDto);

        return signInResultDto;

    }


    @Override
    public SignInResultDto google_SignIn(String accessToken) {
        GoogleResponseDto googleResponseDto = getGoogleInfo(accessToken);

        SignInResultDto signInResultDto = new SignInResultDto();
        if (googleResponseDto == null) {
            return handleSignInFailure(signInResultDto, "Failed to get Kakao user info");
        }

        Member member = authDao.findMember(googleResponseDto.getEmail());

        if (member == null) {
            member = memberService.createGoogleUser(googleResponseDto);
            member.setServiceRole("ROLE_USER");
            authDao.saveMember(member);
            addServiceRoleManager(member);
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setNewUser(true);
            signInResultDto.setDetailMessage("회원가입 완료.");
        } else {
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setNewUser(false);
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
            authDao.saveMember(member); // 변경 사항 저장
        }

        signInResultDto.setToken(accessTokenNew);
        signInResultDto.setRefreshToken(refreshToken); // 최종 Refresh Token 설정

        resultStatusService.setSuccess(signInResultDto);
        signInResultDto.setDetailMessage("로그인 성공.");
        log.info("[SignIn] SignInResultDto: {}", signInResultDto);

        return signInResultDto;

    }

    @Override
    public ResponseEntity<?> refreshAccessToken(String refreshToken) {
        if (jwtProvider.validRefreshToken(refreshToken)) {
            String email = jwtProvider.getUsernameFromRefreshToken(refreshToken);
            String newAccessToken = jwtProvider.createToken(email, List.of("ROLE_USER"));

            // 리프레시 토큰의 유효 기간이 하루 미만일 경우, 새로운 리프레시 토큰을 발급
            if (jwtProvider.getExpirationDuration(refreshToken) < 1000 * 60 * 60 * 24) {
                String newRefreshToken = jwtProvider.createRefreshToken(email);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken));
            } else {
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            }
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
            member.addAuthAdditionalInfo(additionalInfoDto); // 기존 User 객체를 전달하여 새로운 User 객체 생성
            authDao.saveMember(member);
            resultStatusService.setSuccess(resultDto);
        } else {
            resultStatusService.setFail(resultDto);
        }
        return resultDto;
    }


    private SignInResultDto handleSignInFailure(SignInResultDto signInResultDto, String errorMessage) {
        resultStatusService.setFail(signInResultDto);
        signInResultDto.setDetailMessage(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    private void addServiceRoleManager(Member member){
        ServiceRole serviceRole = new ServiceRole();
        serviceRole.setMember(member);
        serviceRole.setRole("Role_User");
        teamMemberDao.saveServiceRole(serviceRole);
    }
    // 전화번호 변환 유틸리티 메서드 추가
    private String formatPhoneNumber(String rawPhoneNumber) {
        if (rawPhoneNumber == null || !rawPhoneNumber.startsWith("+82")) {
            return rawPhoneNumber; // 변환이 필요 없는 경우 원본 반환
        }
        rawPhoneNumber = rawPhoneNumber.replaceAll("\\s+", "");
        // +82 -> 0
        String localPhoneNumber = rawPhoneNumber.replace("+82", "0");

        return localPhoneNumber;
    }
}
